/*
#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";

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

     
behavior Susan(i_uchar7220_receiver in_image, i_uchar7220_sender out_image) 
{

    c_int7220_queue r(1ul);
    c_uchar7220_queue mid(1ul);
//    c_uchar7220_queue mid_edge_draw(1ul);
    c_uchar7220_queue image_edge_draw(1ul);

//    Edges edges(in_image, r, mid, image_edge_draw);
//    Thin thin(r, mid, mid_edge_draw);
//    Draw draw(image_edge_draw, mid_edge_draw, out_image);
    PE1 pe1(in_image, r, mid, image_edge_draw);
    PE2 pe2(r, mid, image_edge_draw, out_image);
        
    void main(void)
    {
        par {
          pe1;
          pe2;
//           edges;
//           thin;
//            draw;
        }      
    }
   
};   
*/

