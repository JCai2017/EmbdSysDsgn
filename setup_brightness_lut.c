#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sys/file.h>

void setup_brightness_lut(unsigned char** bp, int thresh, int form)
{
int   k;
float temp;

  *bp=(unsigned char *)malloc(516);
  *bp=*bp+258;

  for(k=-256;k<257;k++)
  {
    temp=((float)k)/((float)thresh);
    temp=temp*temp;
    if (form==6)
      temp=temp*temp*temp;
    temp=100.0*exp(-temp);
    *(*bp+k)= (unsigned char)temp;
  }
  
}