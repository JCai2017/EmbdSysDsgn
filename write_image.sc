#include <stdio.h>
#include "uchar.h"
import "c_bit8_queue";
import "c_double_handshake";

behavior write_image(i_receiver inputtedOutputImage, i_bit8_sender forwardedOutputImage)
{
  void main(void)
  {
    
    const int x_size=76, y_size=95;
    int k;
 
    for(k=0; k<x_size*y_size; k++)
    {
      uchar c;
      inputtedOutputImage.receive(&c, 1);
      forwardedOutputImage.send(c);
    }  
  }
};
