#include "susan.sh"

import "c_uchar7220_queue_os";
import "c_int7220_queue_os";
import "c_uchar7220_queue";
import "i_receiver";
import "i_sender";

import "susan_thin";
import "detect_edges";
import "edge_draw";
import "OS_channel";
import "Init";
     
behavior Susan(/*i_uchar7220_receiver*/i_receiver in_image, /*i_uchar7220_sender*/i_sender out_image, OSAPI os) implements Init
{

    c_int7220_queue_os r(1ul, os);
    c_uchar7220_queue_os mid(1ul, os);
    c_uchar7220_queue_os mid_edge_draw(1ul, os);
    c_uchar7220_queue_os image_edge_draw(1ul, os);
    int my_id;
    int ed_id, th_id, dr_id;

    Edges edges(in_image, r, mid, image_edge_draw, os);
    Thin thin(r, mid, mid_edge_draw, os);
    Draw draw(image_edge_draw, mid_edge_draw, out_image, os);

    int init(void)
    {
      my_id = os.create();
      return my_id; 
    }
        
    void main(void)
    {
        os.waitTask(my_id);

        ed_id = edges.init();
        th_id = thin.init();
        dr_id = draw.init();

        os.par_start(my_id);
        par {
            edges;
            thin;
            draw;
        }      
        os.par_end(my_id);

        os.kill();
    }
   
};   


