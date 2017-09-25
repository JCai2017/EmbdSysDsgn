#include <stdio.h>
#include <math.h>
#include "uchar.h"
import "c_queue";

behavior setup_brightness_lut(i_sender bp_sender)
{
  const int thresh = 20;
  const int form = 6;
  int k;
  float temp;
  uchar bp[516];
  int idx = 258;

  void main(void)
  {
    printf("setup_brightness_lut\n");
    for (k = -256; k < 257; k++){
      temp = ((float)k)/((float)thresh);
      temp = temp*temp;
      if (form == 6){
        temp = temp*temp*temp;
      }
      temp = 100.0*exp(-temp);
      bp[idx+k] = (uchar)temp; 
    }
    for (k = 0; k < 516; k++){
      bp_sender.send(&bp[k], 1);
    }
  }
};
