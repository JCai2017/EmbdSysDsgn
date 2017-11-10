//////////////////////////////////////////////////////////////////////
// File:   	HWBusPAM.sc
//////////////////////////////////////////////////////////////////////

// Simple hardware bus
#include <stdint.h>

#define ADDR_WIDTH	16u
#define DATA_WIDTH	8u

#if DATA_WIDTH == 32u
# define DATA_BYTES 4u
#elif DATA_WIDTH == 16u
# define DATA_BYTES 2u
#elif DATA_WIDTH == 8u
# define DATA_BYTES 1u
#else
# error "Invalid data width"
#endif


// Protocol primitives
interface IMasterHardwareBusProtocol
{
  void masterWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d);
  void masterRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d);
};

interface ISlaveHardwareBusProtocol
{
  void slaveWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d);
  void slaveRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d);
};


// Bus protocol channel
channel HardwareBusTLM()
  implements IMasterHardwareBusProtocol, ISlaveHardwareBusProtocol
{
  // wires
  //signal unsigned bit[ADDR_WIDTH-1:0] A;
  //signal unsigned bit[DATA_WIDTH-1:0] D;
  //signal unsigned bit[1]    ready = 0;
  //signal unsigned bit[1]    ack = 0;
  uint16_t A;
  uint8_t D;
  event ready;
  event ack;

  // master access methods
  void masterWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d)
  {
    A = a;
    D = d;
    waitfor(15000);
    notify(ready);
    wait(ack);
  }

  void masterRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d)
  {
    /*do {
      t1: A = a;
          waitfor(5000);
      t2: ready = 1;
          while(!ack) wait(ack);
      t3: *d = D;
          waitfor(15000);
      t4: ready = 0;
          while(ack) wait(ack);
    }
    timing {
      range(t1; t2; 5000; 15000);
      range(t3; t4; 10000; 25000);
    }*/
  }

  // slave access methods
  void slaveWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d)
  {
    /*do {
      t1: while(!ready) wait(ready);
      t2: if(a != A) {
            waitfor(1000); // avoid hanging from t2 to t1
            goto t1;
          }
          else {
            D = d;
            waitfor(12000);
          }
      t3: ack = 1;
          while(ready) wait(ready);
      t4: waitfor(7000);
      t5: ack = 0;
    }
    timing {
      range(t2; t3; 10000; 20000);
      range(t4; t5; 5000; 15000);
    }*/
  }

  void slaveRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d)
  {
    int t = 0;
    wait(ready);
    while(a != A){
      wait(ready);
      t++;
    }
    a = A;
    *d = D;
    waitfor(19000 + t*1000);
    notify(ack);
  }
};
