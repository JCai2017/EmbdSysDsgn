import "c_queue";
import "c_handshake";

#include "uchar.h"

behavior read_image(i_receive start, i_receiver stim2read, i_sender img_read2susan)
{

    void main(void){
        uchar in_[76 * 95];
        int i, j;

        for(j = 0; j < 5; j ++){
            for(i = 0; i < (76 * 95); i ++){
                stim2read.receive(&in_[i], 1);
            }

            if(j == 0){
                waitfor(1000);
            }

            start.receive();

            for(i = 0; i < (76 * 95); i ++){
                img_read2susan.send(&in_[i], 1);
            }
        }
     }
};

