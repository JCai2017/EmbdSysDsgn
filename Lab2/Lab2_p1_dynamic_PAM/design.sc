#include "susan.sh"

import "susan";
import "read_image";
import "write_image";
//import "c_uchar7220_queue";
import "c_double_handshake";
import "OS_channel";

behavior PE1(/*i_uchar7220_receiver*/ i_receiver in_image, /*i_uchar7220_sender*/ i_sender out_image)
{
  OS_channel os; 
  Susan TASK_PE1(in_image, out_image, os); // TASK_PE1
  int task_id; 
  
  void main(void) {
    os.init();
    task_id = TASK_PE1.init();  
    os.start(task_id);

    TASK_PE1.main();
  }
};

behavior INPUT(i_receive start, in uchar image_buffer[IMAGE_SIZE], /*i_uchar7220_sender*/ i_sender in_image)
{
  ReadImage read_image(start, image_buffer, in_image);
  
  void main(void) {
    read_image.main();
  }
};

behavior OUTPUT(/*i_uchar7220_receiver*/ i_receiver out_image, i_sender out_image_susan)
{
  WriteImage write_image(out_image, out_image_susan);

  void main(void) {
    write_image.main();
  }
};
 

behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{

    //c_uchar7220_queue in_image(1ul); 
    //c_uchar7220_queue out_image(1ul);
    c_double_handshake in_image;
    c_double_handshake out_image;
    
//    ReadImage read_image(start, image_buffer, in_image);
//    Susan susan(in_image, out_image);
//    WriteImage write_image(out_image, out_image_susan);

    INPUT input(start, image_buffer, in_image);
    PE1 pe1(in_image, out_image);
    OUTPUT output(out_image, out_image_susan);

    void main(void) {
       par {
            input.main();
            pe1.main();
            output.main();
        }
    }
    
};
