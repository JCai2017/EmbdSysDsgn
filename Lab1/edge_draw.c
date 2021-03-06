#include "uchar.h"

edge_draw(uchar* in, uchar *mid, int drawing_mode)
{
int   i, x_size=76, y_size=95;
uchar *inp, *midp;

//for(i = 0; i < x_size*y_size; i++)
//printf("%d ", mid[i]);

  if (drawing_mode==0)
  {
    /* mark 3x3 white block around each edge point */
    midp=mid;
    for (i=0; i<x_size*y_size; i++)
    {
      if (*midp<8) 
      {
        inp = in + (midp - mid) - x_size - 1;
        *inp++=255; *inp++=255; *inp=255; inp+=x_size-2;
        *inp++=255; *inp++;     *inp=255; inp+=x_size-2;
        *inp++=255; *inp++=255; *inp=255;
      }
      midp++;
    }
  }

  /* now mark 1 black pixel at each edge point */
  midp=mid;
  for (i=0; i<x_size*y_size; i++)
  {
    if (*midp<8) 
      *(in + (midp - mid)) = 0;
    midp++;
  }

//for(i = 0; i < x_size*y_size; i++)
//printf("%d ", mid[i]);           

}
