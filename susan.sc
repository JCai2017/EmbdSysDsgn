import "c_queue";

#define Q_SIZE 5

behavior Susan(i_receiver in_image2edges, i_sender in_draw2image) 
{
  c_queue bp(Q_SIZE);
  c_queue in_edges2draw(Q_SIZE);
  c_queue r(Q_SIZE); 
  c_queue mid_edges2thin(Q_SIZE);
  c_queue mid_thin2draw(Q_SIZE);

  setup_brighness_lut sbl(bp);
  susan_edges se(in_image2edge, in_edges2draw, r, mid_edges2thin, bp);
  susan_thin st(r, mid_edges2thin, mid_thin2draw);.
  edge_draw  ed(in_edges2draw, in_draw2image, mid_thin2draw);
  
  void main(void)
  {
    par{
      setup_brightness_lut.main();
      susan_edges.main();
      susan_thin.main();
      edge_draw.main();
    }
  }
}

