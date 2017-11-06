#include "susan.sh"

import "c_uchar7220_queue_os";
import "c_int7220_queue_os";
import "setup_brightness_lut";
import "susan_edges";
import "OS_channel";
import "Init";

behavior DetectEdges(i_uchar7220_receiver in_image,  i_int7220_sender_os out_r, i_uchar7220_sender_os out_mid, i_uchar7220_sender_os out_image, OSAPI os) 
{

    uchar bp[516];
        
    SetupBrightnessLut setup_brightness_lut(bp, os);
    SusanEdges susan_edges(in_image, out_r, out_mid, bp, out_image, os);

    void main(void) {
//printf("starting brightness\n");
        setup_brightness_lut.main(); 
//printf("between brightness and susan edges\n");
        susan_edges.main();

    //printf("DETECT KILLED\n");
    os.print();
    }
};

behavior Edges(i_uchar7220_receiver in_image,  i_int7220_sender_os out_r, i_uchar7220_sender_os out_mid, i_uchar7220_sender_os out_image, OSAPI os) implements Init
{

    DetectEdges detect_edges(in_image,  out_r, out_mid, out_image, os);
    int my_id;

    int init(void) {
      my_id = os.create();
      return my_id;
    }
    
    void main(void) {
//printf("DETECT CREATED\n");
//printf("before edges\n");
        os.waitTask(my_id);
//printf("starting edges\n");
        fsm{
            detect_edges: {goto detect_edges;}
        }
        os.kill();
    }
};

