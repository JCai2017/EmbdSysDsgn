#include "susan.sh"

import "OS_channel";
import "Init";
//#include <sim.sh>
//#include <stdio.h>

behavior SetupBrightnessLutThread(uchar bp[516], in int thID, OSAPI os) implements Init
{

    int my_id;
    int init(void) {
      my_id = os.create();
      return my_id;
    }
       
    void main(void) {
        int   k;
        float temp;
        int thresh, form;
        
        thresh = BT;
        form = 6;
      
        os.waitTask(my_id);
printf("brightness thread satart\n");
        //for(k=-256;k<257;k++)
       for(k=(-256)+512/PROCESSORS*thID; k<(-256)+512/PROCESSORS*thID+512/PROCESSORS+1; k++){
            temp=((float)k)/((float)thresh);
            temp=temp*temp;
            if (form==6)
                temp=temp*temp*temp;
            temp=100.0*exp(-temp);
            bp[(k+258)] = (uchar) temp;
        }
       os.kill();
    }

};
 
behavior SetupBrightnessLut(uchar bp[516], OSAPI os) 
{
       
    SetupBrightnessLutThread setup_brightness_thread_0(bp, 0, os);
    SetupBrightnessLutThread setup_brightness_thread_1(bp, 1, os);
    int my_id;
    int my_id0;
    int my_id1;
       
    void main(void) {
        my_id  = os.getMyID();
        my_id0 = setup_brightness_thread_0.init(); 
        my_id1 = setup_brightness_thread_1.init();       
printf("starting brightness\n");
        os.par_start(my_id);
printf("starting brightness par\n");
        par {
            setup_brightness_thread_0;
            setup_brightness_thread_1;
        }
        os.par_end(my_id);
printf("ending brightness\n");
        os.timewait(2700);
//        printf("%llu\n", now());
    }

};

