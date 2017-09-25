#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "uchar.h"
#include <string.h>

import "c_queue";

#define  exit_error(IFB,IFC) { fprintf(stderr,IFB,IFC); exit(0); }

behavior put_image(i_receiver name_receiver, i_receiver in_receiver)
{
  FILE* fd;
  const int x_size = 76;
  const int y_size = 95; 
  char vals[x_size*y_size];
  char name[200];
  int i;

  void main(void)
  {
    printf("put_image\n");
    for (i = 0; i < 200; i++){
      name_receiver.receive(&name[i], 1);
      if (name[i] == '\0'){ break; }
    }
    for (i = 0; i < x_size*y_size; i++){
      in_receiver.receive(&vals[i], 1);
    }
#ifdef FOPENB
    if ((fd=fopen(name,"wb")) == NULL)
#else
    if ((fd=fopen(name,"w")) == NULL)
#endif
    exit_error("Can't output image%s.\n",name);

    fprintf(fd,"P5\n");
    fprintf(fd,"%d %d\n",x_size,y_size);
    fprintf(fd,"255\n");

    if (fwrite(vals, x_size*y_size,1,fd) != 1){
      exit_error("Can't write image %s.\n", name);
    }

    fclose(fd);

  }
};
