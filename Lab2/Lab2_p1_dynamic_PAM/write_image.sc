#include "susan.sh"

import "i_sender";
import "i_receiver";
//import "c_uchar7220_queue";

behavior WriteImage(/*i_uchar7220_receiver*/ i_receiver in_image, i_sender out_image)
{

    void main(void) {
        int i;       
        uchar image_buffer[IMAGE_SIZE];
        
        while (true) {
            for (i=0; i <IMAGE_SIZE; i++)
              in_image.receive(&image_buffer[i], sizeof(char));
//            in_image.receive(&image_buffer);
            out_image.send(image_buffer, sizeof(image_buffer));       
        }
    }
         
}; 
