#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/file.h>

#include "uchar.h"

import "c_queue";

#define exit_error(IFB, IFC) {fprintf(stderr, IFB, IFC); exit(0); }

behavior get_image(i_sender in_image2edges, i_receiver name)
{
    /* {{{ int getint(fp) derived from XV */

    int getint(FILE fd[1])
    {
      int c, i;
      char dummy[10000];

      c = getc(fd);
      while (1) /* find next integer */
      {
        if (c=='#')    /* if we're at a comment, read to end of line */
          fgets(dummy,9000,fd);
        if (c==EOF)
          exit_error("Image %s not binary PGM.\n","is");
        if (c>='0' && c<='9')
          break;   /* found what we were looking for */
        c = getc(fd);
      }

      /* we're at the start of a number, continue until we hit a non-number */
      i = 0;
      while (1) {
        i = (i*10) + (c - '0');
        c = getc(fd);
        if (c==EOF) return (i);
        if (c<'0' || c>'9') break;
      }

      return (i);
    }

/* }}} */


  void main(void)
  {
    FILE *fd;
    char filename[200];
    uchar in_[76 * 95];
    char header [100];
    int i, tmpx, tmpy, tmp;

    for(i = 0; i < 200; i ++){
      name.receive(&filename[i], 1);
      if(filename[i] == 0)
        break;
    }


#ifdef FOPENB
      if ((fd=fopen(filename,"rb")) == NULL)
#else
      if ((fd=fopen(filename,"r")) == NULL)
#endif
        exit_error("Can't input image %s.\n",filename);

      /* {{{ read header */

      header[0]=fgetc(fd);
      header[1]=fgetc(fd);
      if(!(header[0]=='P' && header[1]=='5'))
        exit_error("Image %s does not have binary PGM header.\n",filename);

      tmpx = getint(fd);
      tmpy = getint(fd);
      tmp = getint(fd);

/* }}} */

      if (fread(in_,1,76 * 95,fd) == 0)
        exit_error("Image %s is wrong size.\n",filename);

      fclose(fd);

      for(i = 0; i < (76 * 95); i ++){
        in_image2edges.send(&in_[i], 1);
      }

  }
};

