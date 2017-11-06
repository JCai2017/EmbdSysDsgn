#include <stdio.h>
#include <string.h>
#include <math.h>
#include "uchar.h"
import "c_bit8_queue";
import "c_bit32_queue";

behavior susan_edges_op1(const int start_j, const int end_j, i_bit8_receiver inputs, i_bit32_sender outputs)
{
  const int y_size = 95;
  const int x_size = 76;

  int max_no = 2650;
  int   i, j, n;
  uchar *p; 
  uchar *cp; 
  uchar* bp;


  uchar in_[x_size* y_size]; 
  int r[x_size * y_size];
  uchar bp_arr[516];
  bit[8] temp8;
  bit[32] temp32;

  void main(void)
  {
    memset (r,0,x_size * y_size * sizeof(int));

    for(i = 0; i < x_size*y_size; i++)
    {
      inputs.receive(&temp8);
      in_[i] = temp8;
    } 
//for(i = 0; i < x_size*y_size; i++)
//printf("%d ",in_[i]);
    for(i = 0; i < 516; i++)
    {
      inputs.receive(&temp8);
      bp_arr[i] = temp8;
    } 
    bp = bp_arr+258;    

    for (i=3;i<y_size-3;i++)
      for (j=start_j;j<end_j;j++)
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

    for(i = 0; i < x_size*y_size; i++)
    {
      temp32 = r[i];
      outputs.send(temp32);
    } 

  } 
};

behavior susan_edges_op2(const int start_j, const int end_j, i_bit8_receiver inputs, i_bit32_receiver inputs1, i_bit8_sender outputs)
{
  const int y_size = 95;
  const int x_size = 76;

  uchar in_[x_size* y_size]; 
  int r[x_size * y_size];
  uchar bp_arr[516];
  uchar* bp;
  uchar mid[x_size * y_size];
  int max_no = 2650;
  bit[8] temp8;
  bit[32] temp32;

  float z;
  int   do_symmetry, i, j, m, n, a, b, x, y, w;
  uchar c;
  uchar *p; 
  uchar *cp;    

  void main(void)
  {
    memset (mid, 100, x_size*y_size);

    for(i = 0; i < x_size*y_size; i++)
    {
      inputs.receive(&temp8);
      in_[i] = temp8;
    } 
    for(i = 0; i < 516; i++)
    {
      inputs.receive(&temp8);
      bp_arr[i] = temp8;
    } 
bp = bp_arr +258;
    for(i = 0; i < x_size*y_size; i++)
    {
      inputs1.receive(&temp32);
      r[i] = temp32;
    } 

  for (i=4;i<y_size-4;i++)
    for (j=start_j;j<end_j;j++)
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

    for(i = 0; i < x_size*y_size; i++)
    {
      temp8 = mid[i];
      outputs.send(temp8);
    } 

  } 
};


behavior susan_edges(i_bit8_receiver bp_receiver, i_bit8_receiver in_image2edges, i_bit32_sender r_sender, i_bit8_sender mid_edges2thin, i_bit8_sender in_edges2draw)
{
const unsigned long Q_SIZE = 76*95*5;
const unsigned long Q_SIZE_2 = (76*95 + 516)*5;
const int _3 = 3;
const int _13 = 13;
const int _23 = 23;
const int _33 = 33;
const int _43 = 43;
const int _53 = 53;
const int _63 = 63;
const int _73 = 73;

c_bit8_queue qin_op1_0(Q_SIZE_2);
c_bit32_queue qout_op1_0(Q_SIZE);
c_bit8_queue qin_op1_1(Q_SIZE_2);
c_bit32_queue qout_op1_1(Q_SIZE);
c_bit8_queue qin_op1_2(Q_SIZE_2);
c_bit32_queue qout_op1_2(Q_SIZE);
c_bit8_queue qin_op1_3(Q_SIZE_2);
c_bit32_queue qout_op1_3(Q_SIZE);
c_bit8_queue qin_op1_4(Q_SIZE_2);
c_bit32_queue qout_op1_4(Q_SIZE);
c_bit8_queue qin_op1_5(Q_SIZE_2);
c_bit32_queue qout_op1_5(Q_SIZE);
c_bit8_queue qin_op1_6(Q_SIZE_2);
c_bit32_queue qout_op1_6(Q_SIZE);

susan_edges_op1 se_op1_0(_3, _13, qin_op1_0, qout_op1_0);
susan_edges_op1 se_op1_1(_13, _23, qin_op1_1, qout_op1_1);
susan_edges_op1 se_op1_2(_23, _33, qin_op1_2, qout_op1_2);
susan_edges_op1 se_op1_3(_33, _43, qin_op1_3, qout_op1_3);
susan_edges_op1 se_op1_4(_43, _53, qin_op1_4, qout_op1_4);
susan_edges_op1 se_op1_5(_53, _63, qin_op1_5, qout_op1_5);
susan_edges_op1 se_op1_6(_63, _73, qin_op1_6, qout_op1_6);

const int _4 = 4;
const int _21 = 21;
const int _38 = 38;
const int _55 = 55;
const int _72 = 72;

c_bit8_queue qin_op2_0(Q_SIZE_2);
c_bit32_queue qin1_op2_0(Q_SIZE);
c_bit8_queue qout_op2_0(Q_SIZE);
c_bit8_queue qin_op2_1(Q_SIZE_2);
c_bit32_queue qin1_op2_1(Q_SIZE);
c_bit8_queue qout_op2_1(Q_SIZE);
c_bit8_queue qin_op2_2(Q_SIZE_2);
c_bit32_queue qin1_op2_2(Q_SIZE);
c_bit8_queue qout_op2_2(Q_SIZE);
c_bit8_queue qin_op2_3(Q_SIZE_2);
c_bit32_queue qin1_op2_3(Q_SIZE);
c_bit8_queue qout_op2_3(Q_SIZE);

susan_edges_op2 se_op2_0(_4, _21, qin_op2_0, qin1_op2_0, qout_op2_0);
susan_edges_op2 se_op2_1(_21, _38, qin_op2_1, qin1_op2_1, qout_op2_1);
susan_edges_op2 se_op2_2(_38, _55, qin_op2_2, qin1_op2_2, qout_op2_2);
susan_edges_op2 se_op2_3(_55, _72, qin_op2_3, qin1_op2_3, qout_op2_3);

  void main(void)
  {
    const int x_size = 76;
    const int y_size = 95;

    uchar in_[x_size* y_size]; 
    int r[x_size * y_size];
    uchar mid[x_size * y_size];
    uchar bp_arr[516];
    uchar* bp;
    int max_no = 2650;
    int k;
    bit[8] temp8;
    bit[32] temp32;

    float z;
    int   do_symmetry, i, j, m, n, a, b, x, y, w;
    uchar c;
    uchar *p; 
    uchar *cp;    

//    printf("susan_edges\n");

    for(k = 0; k < 516; k++)
    {
      bp_receiver.receive(&temp8);
      bp_arr[k] = temp8;
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
      in_image2edges.receive(&temp8);
      in_[k] = temp8;
//printf("%d ", in_[k]);
    }    
//for(k = 0; k < x_size * y_size; k++)
//printf("%d ", in_[k]);



//-----------------------------//

//printf("size of %d\n", sizeof(int));

  memset (r,0,x_size * y_size * sizeof(int));
  memset (mid, 100, x_size*y_size);
    
  
  for(i = 0; i < x_size*y_size; i++)
  {
    temp8 = in_[i];
    qin_op1_0.send(temp8);
    qin_op1_1.send(temp8);
    qin_op1_2.send(temp8);
    qin_op1_3.send(temp8);
    qin_op1_4.send(temp8);
    qin_op1_5.send(temp8);
    qin_op1_6.send(temp8);
  } 
  for(i = 0; i < 516; i++)
  {
    temp8 = bp_arr[i];
    qin_op1_0.send(temp8);
    qin_op1_1.send(temp8);
    qin_op1_2.send(temp8);
    qin_op1_3.send(temp8);
    qin_op1_4.send(temp8);
    qin_op1_5.send(temp8);
    qin_op1_6.send(temp8);
  } 
  par {
    se_op1_0.main();
    se_op1_1.main();
    se_op1_2.main();
    se_op1_3.main();
    se_op1_4.main();
    se_op1_5.main();
    se_op1_6.main();
  }
  for(i = 0; i < x_size*y_size; i++)
  {
    qout_op1_0.receive(&temp32);    
    r[i] += temp32;
    qout_op1_1.receive(&temp32);    
    r[i] += temp32;
    qout_op1_2.receive(&temp32);    
    r[i] += temp32;
    qout_op1_3.receive(&temp32);    
    r[i] += temp32;
    qout_op1_4.receive(&temp32);    
    r[i] += temp32;
    qout_op1_5.receive(&temp32);    
    r[i] += temp32;
    qout_op1_6.receive(&temp32);    
    r[i] += temp32;
  } 
//------------------------------------
 
  for(i = 0; i < x_size*y_size; i++)
  {
    temp8 = in_[i];
    qin_op2_0.send(temp8);
    qin_op2_1.send(temp8);
    qin_op2_2.send(temp8);
    qin_op2_3.send(temp8);
  } 
  for(i = 0; i < 516; i++)
  {
    temp8 = bp_arr[i];
    qin_op2_0.send(temp8);
    qin_op2_1.send(temp8);
    qin_op2_2.send(temp8);
    qin_op2_3.send(temp8);
  } 
  for(i = 0; i < x_size*y_size; i++)
  {
    temp32 = r[i];
    qin1_op2_0.send(temp32);
    qin1_op2_1.send(temp32);
    qin1_op2_2.send(temp32);
    qin1_op2_3.send(temp32);
  } 
  par {
    se_op2_0.main();
    se_op2_1.main();
    se_op2_2.main();
    se_op2_3.main();
  }
  for(i = 0; i < x_size*y_size; i++)
  {
    qout_op2_0.receive(&temp8);    
    if(temp8 != 100)
      mid[i] = temp8;
    qout_op2_1.receive(&temp8);    
    if(temp8 != 100)
      mid[i] = temp8;
    qout_op2_2.receive(&temp8);    
    if(temp8 != 100)
      mid[i] = temp8;
    qout_op2_3.receive(&temp8);    
    if(temp8 != 100)
      mid[i] = temp8;
  } 

//-------------//
//printf("r values out susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      temp32 = r[k];
      r_sender.send(temp32);    
//printf("%d ", r[k]);
    }    
//printf("mid values out susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      temp8 = mid[k];
      mid_edges2thin.send(temp8);    
//printf("%d ", mid[k]);
    }    
//printf("in values out susan edges\n");
    for(k = 0; k < x_size * y_size; k++)
    {
      temp8 = in_[k];
      in_edges2draw.send(temp8);    
//printf("%d ", in_[k]);
    }    
  }

};
