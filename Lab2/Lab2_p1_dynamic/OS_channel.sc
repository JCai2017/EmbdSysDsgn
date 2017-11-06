
interface OSAPI
{
   void notifyTask(int task_ind);
   void waitTask(int task_ind);

   void init();
   
   int start(int task_ind);
   int create();
   int getMyID();
   void dispatch();
   void kill();
   void activate(int task_ind);
   void yield(); 
   void par_start(int task_ind);
   void par_end(int task_ind);
   void timewait(int time);
   int pre_wait(void);
   void post_wait(int task_ind);
   void print(void);
};


#include <stdio.h>

channel OS_channel implements OSAPI{
   
   typedef struct _Task{
     int alive;
     int active; 
   } Task;

   int last_task;
   int cur_task;
   Task Tasks[100];
   event E0;
   event E1;
   event E2;
   event E3;
   event E4;
   event E5;
   event E6;
   event E7;
   event E8;
   event E9;

   void notifyTask(int task_ind)
   {
     switch(task_ind)
     {
       case 0: notify(E0);
               break;
       case 1: notify(E1);
               break;
       case 2: notify(E2);
               break;
       case 3: notify(E3);
               break;
       case 4: notify(E4);
               break;
       case 5: notify(E5);
               break;
       case 6: notify(E6);
               break;
       case 7: notify(E7);
               break;
       case 8: notify(E8);
               break;
       case 9: notify(E9);
               break;
       default:
          while(1);
          printf("not enough events\n");
          break;
     }
   }

   void waitTask(int task_ind)
   {
     switch(task_ind)
     {
       case 0: wait(E0);
               break;
       case 1: wait(E1);
               break;
       case 2: wait(E2);
               break;
       case 3: wait(E3);
               break;
       case 4: wait(E4);
               break;
       case 5: wait(E5);
               break;
       case 6: wait(E6);
               break;
       case 7: wait(E7);
               break;
       case 8: wait(E8);
               break;
       case 9: wait(E9);
               break;
       default:
          printf("not enough events\n");
          while(1);
          break;
     }
   }
        
   void print(void) {
     int i;
     for ( i = 0; i < last_task; i++) {
       printf("Task %d: alive %d, active %d\n", i, Tasks[i].alive, Tasks[i].active);
     }
   }

   void init()
   {
     int i;
     for(i = 0; i < 100; i++)
     {
       Tasks[i].alive = 0;
       Tasks[i].active = 0;
     }
     last_task = 0;
     cur_task = -1;
     // need to clear all Tasks
   }

   int start(int task_ind)
   {
     cur_task = task_ind;
     notifyTask(cur_task);
     return last_task;
   } 
 
   int create()
   {
     int i;     
     int found = 0;

     for(i = 0; i < last_task; i++)
     {       
       if(Tasks[i].alive == 0)
       {
         Tasks[i].alive = 1;
         Tasks[i].active = 1;          
         found = 1;
         break;
       }
     } 
     if(found == 0){
       Tasks[last_task].alive = 1;
       Tasks[last_task].active = 1;
       i = last_task;
       last_task++;
     }
printf("create: %d\n", i);
     return i;
   }

   int getMyID() {
     return cur_task;
   }

   void dispatch()
   {
     int i;
     for(i = 0; i < last_task; i++)
     {
       if(Tasks[(cur_task + i + 1) % last_task].alive == 1)
       {
         if(Tasks[(cur_task + i + 1) % last_task].active == 1)
         { 
           cur_task = (cur_task + i + 1) % last_task;
           notifyTask(cur_task); // should find one active task or will deadlock
printf("dispatch: %d\n", cur_task);
	   break;
         }
       }
     }
print();
   }

   void kill()
   {
     int my_task_ind; 

     my_task_ind = cur_task;
     dispatch(); // start next task
     Tasks[my_task_ind].alive = 0;     
     Tasks[my_task_ind].active = 0;
printf("kill: %d\n", my_task_ind);
   }

   void activate(int task_ind)
   {
     Tasks[task_ind].active = 1;  
   }

   void yield()
   { 
     int my_task_ind;

     my_task_ind = cur_task;
printf("yield ");
     dispatch(); 
     waitTask(my_task_ind);
   }

   void par_start(int task_ind) {
     Tasks[task_ind].active = 0;    
     dispatch(); 
   }

   void par_end(int task_ind) {
      Tasks[task_ind].active = 1;
      if (cur_task != task_ind) dispatch();
      waitTask(task_ind);
   }

   void timewait(int time) {
     waitfor(time);
     yield();
   }

   int pre_wait(void) {
     int my_id;
     my_id = cur_task; 
     Tasks[my_id].active = 0;
     dispatch();
     return my_id;
   }

   void post_wait(int task_ind) {
     Tasks[task_ind].active = 1;
     if (cur_task != task_ind) dispatch();
     waitTask(task_ind);
   }
};
