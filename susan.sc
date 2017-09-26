#include <stdio.h>
import "c_queue";
import "setup_brightness_lut";

behavior susan(i_receiver start, i_receiver in_image2edges, i_sender in_draw2image)
{
  const unsigned long Q_SIZE = 5;

  c_queue bp(Q_SIZE);
  c_queue in_edges2draw(Q_SIZE);
  c_queue r(Q_SIZE); 
  c_queue mid_edges2thin(Q_SIZE);
  c_queue mid_thin2draw(Q_SIZE);

  setup_brightness_lut sbl(bp);
//  susan_edges se(bp, in_image2edges, r, mid_edges2thin, in_edges2draw);
//  susan_thin st(r, mid_edges2thin, mid_thin2draw);
//  edge_draw ed(mid_thin2draw, in_edges2draw, in_draw2image);
  
  void main(void)
  {
    par{
      sbl.main();
//      se.main();
//      st.main();
//      ed.main();
    }
  }
};

