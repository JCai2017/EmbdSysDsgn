//////////////////////////////////////////////////////////////////////
// File:   	HWBus.sc
//////////////////////////////////////////////////////////////////////
#include <stdio.h>
#include <sim.sh>;
#include <stdint.h>
import "i_sender";
import "i_receiver";
import "c_handshake";

// Simple hardware bus

#define ADDR_WIDTH	16u
#define DATA_WIDTH	32u

#if DATA_WIDTH == 32u
# define DATA_BYTES 4u
#elif DATA_WIDTH == 16u
# define DATA_BYTES 2u
#elif DATA_WIDTH == 8u
# define DATA_BYTES 1u
#else
# error "Invalid data width"
#endif

/* ----- Bus TLM layer, bus protocol ----- */

// TLM primitives
interface IMasterHardwareBusTLM
{
  void masterWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d);
  void masterRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d);
};

interface ISlaveHardwareBusTLM
{
  void slaveWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d);
  void slaveRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d);
};

// TLM implementation
channel HardwareBusTLM(unsigned bit[ADDR_WIDTH-1:0] A,
                       unsigned bit[DATA_WIDTH-1:0] D)
  implements IMasterHardwareBusTLM, ISlaveHardwareBusTLM
{
  event ready;
  event ack;  

  void masterWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d)
  {
    A = a;
    D = d;
    waitfor(15000);
    notify(ready);
//printf("master write ready\n");
    wait(ack);
  }

  void masterRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d)
  {
    A = a;
    notify(ready);
//printf("master read ready\n");
    wait(ack); 
    *d = D;
    waitfor(20000);
  }

  void slaveWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d)
  {
    wait(ready);
//printf("first slave write ready\n");
    while( a != A) {
      waitfor(1000);
      wait(ready);
    }
    D = d;
    waitfor(19000);
    notify ack;
  }

  void slaveRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d)
  {
    wait(ready);
//printf("first slave ready ready\n");
    while(a != A){
      waitfor(1000);
      wait(ready);
    }
    *d = D;
    waitfor(19000);
    notify(ack);
  }
};

/* -----  Physical layer, interrupt handling ----- */

channel MasterHardwareSyncDetect(i_receive intr)
  implements i_receive
{
  void receive(void)
  {
      intr.receive();

//      int val;
//      val = 0x01 & intr;
////printf("Master sync waiting %llu %d\n", now(), val);
////    wait(rising intr);
////      wait intr;
//      while(val != 1) { val = 0x01 & intr; waitfor(1000);}
//      intr_clr = 1;
//      waitfor(5000);
//      intr_clr = 0;
////printf("Master sync received\n");
  }
};

channel SlaveHardwareSyncGenerate(i_send intr)
  implements i_send
{
  void send(void)
  {
    intr.send();

//    int val;
//    val = 0x01 & intr_clr;
//    intr = 1;
////printf("Slave sync high\n");
////    waitfor(5000);
////    wait intr_clr;
//    while(val != 1) { val = 0x01 & intr_clr; waitfor(1000);}
////printf("Slave sync low\n");
//    intr = 0;
  }
};


/* -----  Media access layer ----- */

interface IMasterHardwareBusLinkAccess
{
  void MasterRead(int addr, void *data, unsigned long len);
  void MasterWrite(int addr, const void* data, unsigned long len);
};
  
interface ISlaveHardwareBusLinkAccess
{
  void SlaveRead(int addr, void *data, unsigned long len);
  void SlaveWrite(int addr, const void* data, unsigned long len);
};

channel MasterHardwareBusLinkAccess(IMasterHardwareBusTLM tlm)
  implements IMasterHardwareBusLinkAccess
{
  void MasterWrite(int addr, const void* data, unsigned long len)
  {    
    unsigned long i;
    unsigned char *p;
    unsigned bit[DATA_WIDTH-1:0] word = 0;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      word = (word<<8) + *p;
      
      if(!((i+1)%DATA_BYTES)) {
	tlm.masterWrite(addr, word);
	word = 0;
      }
    }
    
    if(i%DATA_BYTES) {
      word <<= 8 * (DATA_BYTES - (i%DATA_BYTES));
      tlm.masterWrite(addr, word);
    }    
  }
  
  void MasterRead(int addr, void* data, unsigned long len)
  {
    unsigned long i;
    unsigned char* p;
    unsigned bit[DATA_WIDTH-1:0] word;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      if(!(i%DATA_BYTES)) {
	tlm.masterRead(addr, &word);
      }

      *p = word[DATA_WIDTH-1:DATA_WIDTH-8];
      word = word << 8;      
    }
  }
};

channel SlaveHardwareBusLinkAccess(ISlaveHardwareBusTLM tlm)
  implements ISlaveHardwareBusLinkAccess
{
  void SlaveWrite(int addr, const void* data, unsigned long len)
  {    
    unsigned long i;
    unsigned char *p;
    unsigned bit[DATA_WIDTH-1:0] word = 0;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      word = (word<<8) + *p;
      
      if(!((i+1)%DATA_BYTES)) {
	tlm.slaveWrite(addr, word);
	word = 0;
      }
    }
    
    if(i%DATA_BYTES) {
      word <<= 8 * (DATA_BYTES - (i%DATA_BYTES));
      tlm.slaveWrite(addr, word);
    }    
  }
  
  void SlaveRead(int addr, void* data, unsigned long len)
  {
    unsigned long i;
    unsigned char* p;
    unsigned bit[DATA_WIDTH-1:0] word;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      if(!(i%DATA_BYTES)) {
	tlm.slaveRead(addr, &word);
      }

      *p = word[DATA_WIDTH-1:DATA_WIDTH-8];
      word = word << 8;      
    }
  }
};


/* -----  Bus instantiation example ----- */

// Bus protocol interfaces
interface IMasterHardwareBus
{
  void MasterRead(uint16_t addr, void *data, unsigned long len);
  void MasterWrite(uint16_t addr, const void* data, unsigned long len);
  
  void MasterSync0Receive();
  void MasterSync1Receive();
};
  
interface ISlaveHardwareBus
{
  void SlaveRead(uint16_t addr, void *data, unsigned long len);
  void SlaveWrite(uint16_t addr, const void* data, unsigned long len);
  
  void SlaveSync0Send();
  void SlaveSync1Send();
};


// Bus TLM channel
channel HardwareBus()
  implements IMasterHardwareBus, ISlaveHardwareBus
{
  // wires
//  signal unsigned bit[ADDR_WIDTH-1:0] A;
//  signal unsigned bit[DATA_WIDTH-1:0] D;
//  signal unsigned bit[1]    ready = 0;
//  signal unsigned bit[1]    ack = 0;
//
//  // interrupts
//  signal unsigned bit[1]    int0 = 0;
//  signal unsigned bit[1]    int1 = 0;
//  signal unsigned bit[1]    int_clr0 = 0;
//  signal unsigned bit[1]    int_clr1 = 0;

  unsigned bit[ADDR_WIDTH-1:0] A; 
//  unsigned bit[DATA_WIDTH-1:0] *D;
  unsigned char D[7220]; 
  event ready;
  event ack;
 
  c_handshake intr0; 
  c_handshake intr1;  

  MasterHardwareSyncDetect  MasterSync0(intr0);
  SlaveHardwareSyncGenerate SlaveSync0(intr0);

  MasterHardwareSyncDetect  MasterSync1(intr1);
  SlaveHardwareSyncGenerate SlaveSync1(intr1);
  
  // TLM Bus instead of physical protocol wires
//  HardwareBusTLM TLM(A, D);

//  MasterHardwareBusLinkAccess MasterLink(TLM);
//  SlaveHardwareBusLinkAccess SlaveLink(TLM);

  
  void MasterRead(uint16_t addr, void *data, unsigned long len) {
    unsigned long i;
    unsigned char* tmp;
    A = addr;
    notify(ready);
//printf("master read ready\n");
    wait(ack);
    tmp = (unsigned char*)data; 
    for(i = 0; i < len; i++){
      tmp[i] = D[i];
    }
    waitfor(20000*len);
 
//    MasterLink.MasterRead(addr, data, len);
  }
  
  void MasterWrite(uint16_t addr, const void *data, unsigned long len) {
    unsigned long i;
    unsigned char* tmp;
    A = addr;
    tmp = (unsigned char*)data; 
    for(i = 0; i < len; i++){
      D[i] = tmp[i];
    }
    waitfor(15000 * len);
    notify(ready);
//printf("master write ready\n");
    wait(ack);

//    MasterLink.MasterWrite(addr, data, len);
  }
  
  void SlaveRead(uint16_t addr, void *data, unsigned long len) {
    unsigned long i;
    unsigned char* tmp;
    wait(ready);
//printf("first slave ready ready\n");
    while(addr != A){
      waitfor(1000);
      wait(ready);
    }
    tmp = (unsigned char*)data; 
    for(i = 0; i < len; i++){
      tmp[i] = D[i];
    }
    waitfor(19000 * len);
    notify(ack);
 
//    SlaveLink.SlaveRead(addr, data, len);
  }
  
  void SlaveWrite(uint16_t addr, const void *data, unsigned long len) {
    unsigned long i;
    unsigned char* tmp;
    wait(ready);
//printf("first slave write ready\n");
    while( addr != A) {
      waitfor(1000);
      wait(ready);
    }
    tmp = (unsigned char*)data; 
    for(i = 0; i < len; i++){
      D[i] = tmp[i];
    }
    waitfor(19000 * len);
    notify ack;
 
//    SlaveLink.SlaveWrite(addr, data, len);
  }

  void MasterSync0Receive() {
    MasterSync0.receive();
  }
  
  void SlaveSync0Send() {
    SlaveSync0.send();
  }

  void MasterSync1Receive() {
    MasterSync1.receive();
  }
  
  void SlaveSync1Send() {
    SlaveSync1.send();
  }
};

channel MasterDriver(IMasterHardwareBus bus, const int address) implements i_sender, i_receiver
{
  void send(const void *d, unsigned long l) {
//printf("master %d, send start\n", address);
    if (address == 0) {
      bus.MasterSync0Receive();
//printf("master %d, send between\n", address);
      bus.MasterWrite(address, d, l); 
    } else { // address == 1 
      bus.MasterSync1Receive();
//printf("master %d, send between\n", address);
      bus.MasterWrite(address, d, l);
    }
//printf("master %d, send end\n", address);
  }
  void receive(void *d, unsigned long l) {
//printf("master %d, receive start\n", address);
    if (address == 0) {
      bus.MasterSync0Receive();
//printf("master %d, receive between\n", address);
      bus.MasterRead(address, d, l); 
    } else { // address == 1 
      bus.MasterSync1Receive();
//printf("master %d, receive between\n", address);
      bus.MasterRead(address, d, l);
    }
//printf("master %d, receive end\n", address);
  }
};

channel SlaveDriver(ISlaveHardwareBus bus, const int address) implements i_sender, i_receiver
{
  void send(const void *d, unsigned long l) {
//printf("slave %d, send start\n", address);
    if (address == 0) {
      bus.SlaveSync0Send();
//printf("slave %d, send between\n", address);
      bus.SlaveWrite(address, d, l); 
    } else { // address == 1 
      bus.SlaveSync1Send();
//printf("slave %d, send between\n", address);
      bus.SlaveWrite(address, d, l);
    }
//printf("slave %d, send end\n", address);
  }
  void receive(void *d, unsigned long l) {
//printf("slave %d, receive start\n", address);
    if (address == 0) {
      bus.SlaveSync0Send();
//printf("slave %d, receive between\n", address);
      bus.SlaveRead(address, d, l); 
    } else { // address == 1 
      bus.SlaveSync1Send();
//printf("slave %d, receive between\n", address);
      bus.SlaveRead(address, d, l);
    }
//printf("slave %d, receive end\n", address);
  }
};
