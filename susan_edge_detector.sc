
#include <stdio.h>
#include <string.h>

import "c_bit8_queue";
import "c_handshake";
import "design";
import "get_image";
import "put_image";

behavior Main(void)
{

    const unsigned long Q_SIZE = 76*100;
    
    c_handshake start;
    c_bit8_queue input(Q_SIZE);
    c_bit8_double_handshake output;
    c_bit8_queue inputName(Q_SIZE);
    c_bit8_queue outputName(Q_SIZE);
    
    design d(start, input, output);
    get_image gi(input, inputName, start);
    put_image pi(outputName, output);

    int main(int argc, char* argv[])
    {
    	int i = 0;
    	int j = 0;
    	if (argc < 3) {
    		printf("Error, too few arguments, exiting.\n");
    		return(1);
    	}
    	if (strlen(argv[1]) > Q_SIZE || strlen(argv[2]) > Q_SIZE) {
    		printf("Error, filenames too long, exiting.\n");
    		return(1);
    	}

		while (1){
	  	  inputName.send(argv[1]+i, 1);
                  if(*(argv[1] + i) == '\0')
                    break;
                  i += 1;
		}
	    while (1){
	    	outputName.send(argv[2]+j, 1);
                if(*(argv[2] + j) == '\0') break;
	    	j += 1;
	    }

    	par {
    		gi.main();
    		d.main();
    		pi.main();
    	}
    	return(0);
    }
};

// EOF
