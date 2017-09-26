
#include <stdio.h>
#include <string.h>

import "c_queue";
import "c_handshake";
// import "susan";
import "get_image";
import "put_image";

behavior Main(void)
{

	const unsigned long Q_SIZE = 50;

	c_handshake start;
	c_queue input(Q_SIZE);
	c_queue output(Q_SIZE);
	c_queue inputName(Q_SIZE);
	c_queue outputName(Q_SIZE);

    // susan s(start, input, output);
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

		while (*(argv[1] + i) != '\0'){
			inputName.send(argv[1]+i, 1);
			i += 1;
		}
	    while (*(argv[2] + j) != '\0'){
	    	outputName.send(argv[2]+j, 1);
	    	j += 1;
	    }

    	par {
    		gi.main();
    		// s.main();
    		pi.main();
    	}
    	return(0);
    }
};

// EOF