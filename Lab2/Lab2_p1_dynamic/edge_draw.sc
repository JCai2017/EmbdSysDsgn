#include "susan.sh"

import "c_uchar7220_queue_os";
import "c_uchar7220_queue";
import "OS_channel";
import "Init";

behavior EdgeDrawThread_PartA(uchar image_buffer[7220], uchar mid[7220], in int thID, OSAPI os) implements Init
{
    int my_id;

    int init(void) {
      my_id = os.create();
      return my_id;
    }

    void main(void) {
    
        int   i;
        uchar *inp, *midp;
        int drawing_mode;
        
        os.waitTask(my_id);
        drawing_mode = DRAWING_MODE;
        if (drawing_mode==0)
        {
            /* mark 3x3 white block around each edge point */
            midp=mid + IMAGE_SIZE/PROCESSORS *thID;
            for (i=X_SIZE*Y_SIZE/PROCESSORS*thID; i<X_SIZE*Y_SIZE/PROCESSORS*(thID+1) + (thID+1==PROCESSORS && X_SIZE*Y_SIZE%PROCESSORS!=0 ?X_SIZE*Y_SIZE%PROCESSORS : 0); i++)
            {
                if (*midp<8) 
                {
                    inp = image_buffer + (midp - mid) - X_SIZE - 1;
                    *inp++=255; *inp++=255; *inp=255; inp+=X_SIZE-2;
                    *inp++=255; *inp++;     *inp=255; inp+=X_SIZE-2;
                    *inp++=255; *inp++=255; *inp=255;
                }
                midp++;
            }
        }
        os.kill();
     }   
   
};    


behavior EdgeDrawThread_PartB(uchar image_buffer[7220], uchar mid[7220], in int thID, OSAPI os) implements Init
{
    int my_id;

    int init(void) {
      my_id = os.create();
      return my_id;
    }

    void main(void) {
    
        int   i;
        uchar  *midp;
        int drawing_mode;
       
        os.waitTask(my_id); 
        drawing_mode = DRAWING_MODE;
     
        /* now mark 1 black pixel at each edge point */
        midp=mid+ IMAGE_SIZE/PROCESSORS *thID;
        //for (i=0; i<X_SIZE*Y_SIZE; i++)
        for (i=X_SIZE*Y_SIZE/PROCESSORS*thID; i<X_SIZE*Y_SIZE/PROCESSORS*(thID+1) + (thID+1==PROCESSORS && X_SIZE*Y_SIZE%PROCESSORS!=0 ?X_SIZE*Y_SIZE%PROCESSORS : 0); i++)
        {
            if (*midp<8) 
                *(image_buffer+ (midp - mid)) = 0;
            midp++;
        }
        os.kill();
    }
    
};    

behavior EdgeDraw_ReadInput(i_uchar7220_receiver_os in_image, i_uchar7220_receiver_os in_mid, uchar image_buffer[IMAGE_SIZE], uchar mid[IMAGE_SIZE])
{
    void main(void) {
//printf("edge draw, read input, in receive\n");
        in_image.receive(&image_buffer);
//printf("edge draw, read input, mid receive\n");
        in_mid.receive(&mid);
//printf("edge draw, read input, done\n");
    }      
};

behavior EdgeDraw_WriteOutput(uchar image_buffer[IMAGE_SIZE],  i_uchar7220_sender out_image)
{
    void main(void) {
        out_image.send(image_buffer);
    }
};

behavior EdgeDraw_PartA(uchar image_buffer[7220], uchar mid[7220], OSAPI os)
{

    EdgeDrawThread_PartA edge_draw_a_thread_0(image_buffer, mid, 0, os);
    EdgeDrawThread_PartA edge_draw_a_thread_1(image_buffer, mid, 1, os);
    int my_id, my_id0, my_id1;   

    void main(void) {
      my_id = os.getMyID();
      my_id0 = edge_draw_a_thread_0.init(); 
      my_id1 = edge_draw_a_thread_1.init();
     
      os.par_start(my_id);
      par {
            edge_draw_a_thread_0;
            edge_draw_a_thread_1;
        }    
      os.par_end(my_id);
      os.timewait(12000000);
    }     
};

behavior EdgeDraw_PartB(uchar image_buffer[7220], uchar mid[7220], OSAPI os)
{

    EdgeDrawThread_PartB edge_draw_b_thread_0(image_buffer, mid, 0, os);
    EdgeDrawThread_PartB edge_draw_b_thread_1(image_buffer, mid, 1, os);
    int my_id, my_id0, my_id1;
    
    void main(void) {
      my_id = os.getMyID();
      my_id0 = edge_draw_b_thread_0.init(); 
      my_id1 = edge_draw_b_thread_1.init();

      os.par_start(my_id);
      par {
            edge_draw_b_thread_0;
            edge_draw_b_thread_1;
        }    
      os.par_end(my_id);
      os.timewait(12000000);
    }     
};


behavior EdgeDraw(i_uchar7220_receiver_os in_image, i_uchar7220_receiver_os in_mid,  i_uchar7220_sender out_image, OSAPI os)
{

    
    uchar image_buffer[IMAGE_SIZE];
    uchar mid[IMAGE_SIZE];         
    
    EdgeDraw_ReadInput edge_draw_read_input(in_image, in_mid, image_buffer, mid);
    EdgeDraw_WriteOutput edge_draw_write_output(image_buffer, out_image);
    EdgeDraw_PartA edge_draw_a(image_buffer, mid, os);
    EdgeDraw_PartB edge_draw_b(image_buffer, mid, os);

    
    void main(void) {
    
        fsm{
            edge_draw_read_input: goto edge_draw_a;
            edge_draw_a: goto edge_draw_b;
            edge_draw_b: goto edge_draw_write_output;
            edge_draw_write_output: {}
        }
//printf("DRAW KILLED\n");
    }     
    
};    

behavior Draw(i_uchar7220_receiver_os in_image, i_uchar7220_receiver_os in_mid,  i_uchar7220_sender out_image, OSAPI os) implements Init
{

    EdgeDraw edge_draw(in_image, in_mid,  out_image, os);
    int my_id;

    int init(void) {
      my_id = os.create();
      return my_id;
    }
    
    void main(void) {
//printf("DRAW CREATED\n");
        os.waitTask(my_id);
//printf("starting edge draw\n");
        fsm {
            edge_draw: {goto edge_draw;}
        }
        os.kill();
    }
    
};


