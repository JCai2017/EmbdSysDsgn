//////////////////////////////////////////////////////////////////////
// SpecC source code generated by SpecC V2.2.1
// Design: c_bit8_queue
// File:   c_bit8_queue_dbg.sc
// Time:   Mon Oct  9 03:37:51 2017
//////////////////////////////////////////////////////////////////////

// Note: Line infos are suppressed in this file.

// Note: User-defined include files are inlined in this file.

// Note: System-defined include files are inlined in this file.

// Note: Imported design files are inlined in this file.

// type synonym definitions //////////////////////////////////////////

typedef long int ptrdiff_t;
typedef unsigned long int size_t;
typedef int wchar_t;

// interface declarations ////////////////////////////////////////////

interface i_bit8_tranceiver;
note i_bit8_tranceiver._SCE_STANDARD_LIB = 
{ "i_tranceiver","bit8","bit[8]" };
interface i_bit8_sender;
note i_bit8_sender._SCE_STANDARD_LIB = 
{ "i_sender","bit8","bit[8]" };
interface i_bit8_receiver;
note i_bit8_receiver._SCE_STANDARD_LIB = 
{ "i_receiver","bit8","bit[8]" };

// channel declarations //////////////////////////////////////////////

channel c_bit8_queue(
    in const unsigned long int) implements i_bit8_sender, i_bit8_receiver, i_bit8_tranceiver;
note c_bit8_queue._SCE_STANDARD_LIB = 
{ "c_queue","bit8","bit[8]" };

// variable and function declarations ////////////////////////////////

extern void perror(const char *);
extern void *memcpy(void *, const void *, unsigned long int);
extern void abort(void);
extern void *malloc(unsigned long int);
extern void free(void *);

// interface definitions /////////////////////////////////////////////

interface i_bit8_tranceiver
{
    void receive(bit[7:0] *);
    void send(bit[7:0]);
};

interface i_bit8_sender
{
    void send(bit[7:0]);
};

interface i_bit8_receiver
{
    void receive(bit[7:0] *);
};

// behavior and channel definitions //////////////////////////////////

channel c_bit8_queue(
    in const unsigned long int size) implements i_bit8_sender, i_bit8_receiver, i_bit8_tranceiver
{
    void setup(void);

    bit[7:0] *buffer = 0;
    unsigned long int n = 0ul;
    unsigned long int p = 0ul;
    event r;
    event s;
    unsigned long int wr = 0ul;
    unsigned long int ws = 0ul;

    void cleanup(void)
    {   
	if ( !n)
	{   
	    free(buffer);
	    buffer = 0;
	}
    }

    void receive(bit[7:0] *d)
    {   
	while( !n)
	{   
	    wr++ ;
	    wait(r);
	    wr-- ;
	}
	if (n <= p)
	{   
	     *d = buffer[p - n];
	}
	else 
	{   
	     *d = buffer[p + size - n];
	}
	n-- ;
	if (ws)
	{   
	    notify(s);
	}
	cleanup();
    }

    void send(bit[7:0] d)
    {   
	while(n >= size)
	{   
	    ws++ ;
	    wait(s);
	    ws-- ;
	}
	setup();
	buffer[p] = d;
	p++ ;
	if (p >= size)
	{   
	    p = 0;
	}
	n++ ;
	if (wr)
	{   
	    notify(r);
	}
    }

    void setup(void)
    {   
	if ( !buffer)
	{   
	    bit[7:0] dummy;
	    unsigned long int i;

	    if ( !(buffer = (bit[7:0] *)malloc(sizeof(bit[7:0]) * size)))
	    {   
		perror("c_typed_queue");
		abort();
	    }
	    for(i = 0; i < size; i++ )
	    {   
		memcpy( &buffer[i],  &dummy, sizeof(bit[7:0]));
	    }
	}
    }
};

//////////////////////////////////////////////////////////////////////
// End of file c_bit8_queue_dbg.sc
//////////////////////////////////////////////////////////////////////
