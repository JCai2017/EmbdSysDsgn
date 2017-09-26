#include <stdio.h>
#include "uchar.h"
import "c_queue";

behavior edge_draw(i_receiver mid_thin2draw, i_receiver in_edges2draw, i_sender in_draw2image)
{
  void main(void)
  {
    
  int   i;
  const int x_size=76, y_size=95;
  int k;
  uchar *inp, *midp;
 
  uchar in_[x_size*y_size]; 
  uchar mid[x_size*y_size];
  
//printf("mid values in edge draw");
  for(k=0; k<x_size*y_size; k++)
  {
    mid_thin2draw.receive(&mid[k], 1);
//printf("%d", mid[k]);
  }
//printf("in values in edge draw");
  for(k=0; k<x_size*y_size; k++)
  {
    in_edges2draw.receive(&in_[k], 1);
//printf("%d", in[k]);
  }
  

  printf("edges_draw\n");
  
    /* mark 3x3 white block around each edge point */
    midp=mid;
    for (i=0; i<x_size*y_size; i++)
    {
      if (*midp<8)
      {
        inp = in_ + (midp - mid) - x_size - 1;
        *inp++=255; *inp++=255; *inp=255; inp+=x_size-2;
        *inp++=255; *inp++;     *inp=255; inp+=x_size-2;
        *inp++=255; *inp++=255; *inp=255;
      }
      midp++;
    }
  
    /* now mark 1 black pixel at each edge point */
    midp=mid;
    for (i=0; i<x_size*y_size; i++)
    {
      if (*midp<8)
        *(in_ + (midp - mid)) = 0;
      midp++;
    }  

//printf("in values in edge draw");
  for(k=0; k<x_size*y_size; k++)
  {
    in_draw2image.send(&in_[k], 1);
//printf("%d", in[k]);
  }
    
    
  }
};
