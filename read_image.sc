import "c_bit8_queue";
import "c_handshake";

#include "uchar.h"
#include <stdio.h>

behavior read_image(i_receive start, i_bit8_receiver stim2read, i_bit8_sender img_read2susan)
{

    void main(void){
      uchar in_[76 * 95];
      int i;
      static int j = 0;

      for(i = 0; i < (76 * 95); i ++){
          stim2read.receive(&in_[i]);
      }

      if(j == 0){
          waitfor(1000);
          j++;
      }

      start.receive();

      for(i = 0; i < (76 * 95); i ++){
          img_read2susan.send(in_[i]);
      }
   }
};

