CC = gcc
CMP = diff -s
RM  = rm -rf

GOLDFILE = golden.pgm
INFILE =   input_small.pgm
OUTFILE =  output_edge.pgm



susan: susan.c setup_brightness_lut.c Makefile
	$(CC) -O4 -o susan susan.c setup_brightness_lut.c -lm 

test:
	./susan $(INFILE) $(OUTFILE) -e
	$(CMP) $(OUTFILE) $(GOLDFILE)

clean:
	$(RM) susan $(OUTFILE)
