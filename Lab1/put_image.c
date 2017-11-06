#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

#define  exit_error(IFB,IFC) { fprintf(stderr,IFB,IFC); exit(0); }

put_image(char filename[100], char* in)
{
FILE  *fd;
int x_size = 76;
int y_size = 95;
#ifdef FOPENB
  if ((fd=fopen(filename,"wb")) == NULL) 
#else
  if ((fd=fopen(filename,"w")) == NULL) 
#endif
    exit_error("Can't output image%s.\n",filename);

  fprintf(fd,"P5\n");
  fprintf(fd,"%d %d\n",x_size,y_size);
  fprintf(fd,"255\n");
  
  if (fwrite(in,x_size*y_size,1,fd) != 1)
    exit_error("Can't write image %s.\n",filename);

  fclose(fd);
}