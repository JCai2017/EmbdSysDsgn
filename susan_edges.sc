#include <stdio.h>
#include <string.h>
#include <math.h>
#include "uchar.h"
import "c_bit8_queue";
import "c_bit32_queue";

behavior susan_edges(i_bit8_receiver bp_receiver, i_bit8_receiver in_image2edges, i_bit32_sender r_sender, i_bit8_sender mid_edges2thin, i_bit8_sender in_edges2draw)
{
  void main(void)
  {
    const int x_size = 76;
    const int y_size = 95;

    uchar in_[x_size* y_size]; 
    int   r[x_size * y_size];
    uchar mid[x_size * y_size];
    uchar bp_arr[516];
    uchar* bp;
    int max_no = 2650;
    int k;

    float z;
    int   do_symmetry, i, j, m, n, a, b, x, y, w;
    uchar c;
    uchar *p; 
    uchar *cp;    

//    printf("susan_edges\n");

    for(k = 0; k < 516; k++)
    {
      bp_receiver.receive(&bp_arr[k], 1);
//printf("%x ", bp_arr[k]);
    } 
bp = bp_arr+258;
//    for(k = 0; k < 258; k++)
//    {
//printf("%x ", *(bp - k));
//    } 

//printf("in values in susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      in_image2edges.receive(&in_[k], 1);    
//printf("%d ", in_[k]);
    }    

//-----------------------------//

//printf("size of %d\n", sizeof(int));

  memset (r,0,x_size * y_size * sizeof(int));
  memset (mid, 100, x_size*y_size);

  for (i=3;i<y_size-3;i++)
    for (j=3;j<x_size-3;j++)
    {
      n=100;
      p=in_ + (i-3)*x_size + j - 1;
      cp=bp + in_[i*x_size+j];

      
//printf("%x, %x, %x, %x, %x, %x, %x\n", n, *in_, *p, cp, *bp, *(cp-*p), in_[i*x_size+j]);
      n+=*(cp-*p);
      p++;
//printf("%d, %c, %c, %c, %c, %c\n", n, *in_, *p, *cp, *bp, *(cp-*p));
      n+=*(cp-*p);
      p++;
//printf("%d, %c, %c, %c, %c, %c\n", n, *in_, *p, *cp, *bp, *(cp-*p));
      n+=*(cp-*p);
      p+=x_size-3; 
//printf("%d, %c, %c, %c, %c, %c\n", n, *in_, *p, *cp, *bp, *(cp-*p));


      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-5;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-6;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=2;
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-6;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-5;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);
      p+=x_size-3;

      n+=*(cp-*p++);
      n+=*(cp-*p++);
      n+=*(cp-*p);

      if (n<=max_no)
        r[i*x_size+j] = max_no - n;
    }

  for (i=4;i<y_size-4;i++)
    for (j=4;j<x_size-4;j++)
    {
      if (r[i*x_size+j]>0)
      {
        m=r[i*x_size+j];
        n=max_no - m;
        cp=bp + in_[i*x_size+j];

        if (n>600)
        {
          p=in_ + (i-3)*x_size + j - 1;
          x=0;y=0;

          c=*(cp-*p++);x-=c;y-=3*c;
          c=*(cp-*p++);y-=3*c;
          c=*(cp-*p);x+=c;y-=3*c;
          p+=x_size-3; 
    
          c=*(cp-*p++);x-=2*c;y-=2*c;
          c=*(cp-*p++);x-=c;y-=2*c;
          c=*(cp-*p++);y-=2*c;
          c=*(cp-*p++);x+=c;y-=2*c;
          c=*(cp-*p);x+=2*c;y-=2*c;
          p+=x_size-5;
    
          c=*(cp-*p++);x-=3*c;y-=c;
          c=*(cp-*p++);x-=2*c;y-=c;
          c=*(cp-*p++);x-=c;y-=c;
          c=*(cp-*p++);y-=c;
          c=*(cp-*p++);x+=c;y-=c;
          c=*(cp-*p++);x+=2*c;y-=c;
          c=*(cp-*p);x+=3*c;y-=c;
          p+=x_size-6;

          c=*(cp-*p++);x-=3*c;
          c=*(cp-*p++);x-=2*c;
          c=*(cp-*p);x-=c;
          p+=2;
          c=*(cp-*p++);x+=c;
          c=*(cp-*p++);x+=2*c;
          c=*(cp-*p);x+=3*c;
          p+=x_size-6;
    
          c=*(cp-*p++);x-=3*c;y+=c;
          c=*(cp-*p++);x-=2*c;y+=c;
          c=*(cp-*p++);x-=c;y+=c;
          c=*(cp-*p++);y+=c;
          c=*(cp-*p++);x+=c;y+=c;
          c=*(cp-*p++);x+=2*c;y+=c;
          c=*(cp-*p);x+=3*c;y+=c;
          p+=x_size-5;

          c=*(cp-*p++);x-=2*c;y+=2*c;
          c=*(cp-*p++);x-=c;y+=2*c;
          c=*(cp-*p++);y+=2*c;
          c=*(cp-*p++);x+=c;y+=2*c;
          c=*(cp-*p);x+=2*c;y+=2*c;
          p+=x_size-3;

          c=*(cp-*p++);x-=c;y+=3*c;
          c=*(cp-*p++);y+=3*c;
          c=*(cp-*p);x+=c;y+=3*c;

          z = sqrt((float)((x*x) + (y*y)));
          if (z > (0.9*(float)n)) /* 0.5 */
	  {
            do_symmetry=0;
            if (x==0)
              z=1000000.0;
            else
              z=((float)y) / ((float)x);
            if (z < 0) { z=-z; w=-1; }
            else w=1;
            if (z < 0.5) { /* vert_edge */ a=0; b=1; }
            else { if (z > 2.0) { /* hor_edge */ a=1; b=0; }
            else { /* diag_edge */ if (w>0) { a=1; b=1; }
                                   else { a=-1; b=1; }}}
            if ( (m > r[(i+a)*x_size+j+b]) && (m >= r[(i-a)*x_size+j-b]) &&
                 (m > r[(i+(2*a))*x_size+j+(2*b)]) && (m >= r[(i-(2*a))*x_size+j-(2*b)]) )
              mid[i*x_size+j] = 1;
          }
          else
            do_symmetry=1;
        }
        else 
          do_symmetry=1;

        if (do_symmetry==1)
	{ 
          p=in_ + (i-3)*x_size + j - 1;
          x=0; y=0; w=0;

          /*   |      \
               y  -x-  w
               |        \   */

          c=*(cp-*p++);x+=c;y+=9*c;w+=3*c;
          c=*(cp-*p++);y+=9*c;
          c=*(cp-*p);x+=c;y+=9*c;w-=3*c;
          p+=x_size-3; 
  
          c=*(cp-*p++);x+=4*c;y+=4*c;w+=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w+=2*c;
          c=*(cp-*p++);y+=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w-=2*c;
          c=*(cp-*p);x+=4*c;y+=4*c;w-=4*c;
          p+=x_size-5;
    
          c=*(cp-*p++);x+=9*c;y+=c;w+=3*c;
          c=*(cp-*p++);x+=4*c;y+=c;w+=2*c;
          c=*(cp-*p++);x+=c;y+=c;w+=c;
          c=*(cp-*p++);y+=c;
          c=*(cp-*p++);x+=c;y+=c;w-=c;
          c=*(cp-*p++);x+=4*c;y+=c;w-=2*c;
          c=*(cp-*p);x+=9*c;y+=c;w-=3*c;
          p+=x_size-6;

          c=*(cp-*p++);x+=9*c;
          c=*(cp-*p++);x+=4*c;
          c=*(cp-*p);x+=c;
          p+=2;
          c=*(cp-*p++);x+=c;
          c=*(cp-*p++);x+=4*c;
          c=*(cp-*p);x+=9*c;
          p+=x_size-6;
    
          c=*(cp-*p++);x+=9*c;y+=c;w-=3*c;
          c=*(cp-*p++);x+=4*c;y+=c;w-=2*c;
          c=*(cp-*p++);x+=c;y+=c;w-=c;
          c=*(cp-*p++);y+=c;
          c=*(cp-*p++);x+=c;y+=c;w+=c;
          c=*(cp-*p++);x+=4*c;y+=c;w+=2*c;
          c=*(cp-*p);x+=9*c;y+=c;w+=3*c;
          p+=x_size-5;
 
          c=*(cp-*p++);x+=4*c;y+=4*c;w-=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w-=2*c;
          c=*(cp-*p++);y+=4*c;
          c=*(cp-*p++);x+=c;y+=4*c;w+=2*c;
          c=*(cp-*p);x+=4*c;y+=4*c;w+=4*c;
          p+=x_size-3;

          c=*(cp-*p++);x+=c;y+=9*c;w-=3*c;
          c=*(cp-*p++);y+=9*c;
          c=*(cp-*p);x+=c;y+=9*c;w+=3*c;

          if (y==0)
            z = 1000000.0;
          else
            z = ((float)x) / ((float)y);
          if (z < 0.5) { /* vertical */ a=0; b=1; }
          else { if (z > 2.0) { /* horizontal */ a=1; b=0; }
          else { /* diagonal */ if (w>0) { a=-1; b=1; }
                                else { a=1; b=1; }}}
          if ( (m > r[(i+a)*x_size+j+b]) && (m >= r[(i-a)*x_size+j-b]) &&
               (m > r[(i+(2*a))*x_size+j+(2*b)]) && (m >= r[(i-(2*a))*x_size+j-(2*b)]) )
            mid[i*x_size+j] = 2;	
        }
      }
    }
   

//-------------//
//printf("r values out susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      r_sender.send(&r[k], 4);    
//printf("%d ", r[k]);
    }    
//printf("mid values out susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      mid_edges2thin.send(&mid[k], 1);    
//printf("%d ", mid[k]);
    }    
//printf("in values out susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      in_edges2draw.send(&in_[k], 1);    
//printf("%d ", in_[k]);
    }    
  }

};
