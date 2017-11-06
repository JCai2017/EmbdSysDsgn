#include "susan.sh"

import "read_image";
import "write_image";

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";

behavior INPUT(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_uchar7220_sender in_image)
{
  ReadImage read_image(start, image_buffer, in_image);
  
  void main(void) {
    read_image.main();
  }
};

behavior PE1(i_uchar7220_receiver in_image, i_int7220_sender r, i_uchar7220_sender mid, i_uchar7220_sender image_edge_draw)
{
  Edges edges(in_image, r, mid, image_edge_draw);
  
  void main(void)
  {
    edges;
  }
};

behavior PE2(i_int7220_receiver r, i_uchar7220_receiver mid, i_uchar7220_receiver image_edge_draw, i_uchar7220_sender out_image)
{
  c_uchar7220_queue mid_edge_draw(1ul);

  Thin thin(r, mid, mid_edge_draw);
  Draw draw(image_edge_draw, mid_edge_draw, out_image);

  void main(void)
  {
    par {
      thin;
      draw;
    }
   }
};

behavior OUTPUT(i_uchar7220_receiver out_image, i_sender out_image_susan)
{
  WriteImage write_image(out_image, out_image_susan);

  void main(void) {
    write_image.main();
  }
};
 

behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{

    c_uchar7220_queue in_image(1ul);
    c_uchar7220_queue out_image(1ul);

    c_int7220_queue r(1ul);
    c_uchar7220_queue mid(1ul);
    c_uchar7220_queue image_edge_draw(1ul);

    INPUT input(start, image_buffer, in_image);
    PE1 pe1(in_image, r, mid, image_edge_draw);
    PE2 pe2(r, mid, image_edge_draw, out_image);
    OUTPUT output(out_image, out_image_susan);

    void main(void) {
       par {
            input.main();
            pe1.main();
            pe2.main();
            output.main();
        }
    }
    
};
