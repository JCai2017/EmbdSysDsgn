import "c_bit8_queue";
import "c_handshake";
import "c_double_handshake";
import "read_image";
import "susan";
import "write_image";

behavior design(i_receive start, i_bit8_receiver img_stim2read, i_sender img_write2mon)
{
  const unsigned long Q_SIZE = 76*100;
  c_bit8_queue img_read2susan(Q_SIZE);
  c_bit8_queue img_susan2write(Q_SIZE);

  read_image ri(start, img_stim2read, img_read2susan);
  susan s(img_read2susan, img_susan2write);
  write_image wi(img_susan2write, img_write2mon);

  void main(void)
  {
    int i;
    for(i = 0; i < 5; i++){
      par{
        ri.main();
        s.main();
        wi.main();
      }
    }
  } 
};
