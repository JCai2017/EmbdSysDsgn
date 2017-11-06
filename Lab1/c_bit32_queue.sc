// c_bit32_queue.sc:	instantiation of template c_typed_queue.sh
//			with type 'bit[32]'
//
// author:      Rainer Doemer, Sarang Bhadsavle
// last update: 10/08/2017

// note:
// this file "instantiates" the "templates" for type 'bit[32]';
// the "templates" are actually implemented as preprocessor macros;
// they can be "instantiated" by calling the macro with parameters;
//

#include <c_typed_queue.sh>	/* make the template available */

// define the tranceiver interface for data type 'bit[32]'
DEFINE_I_TYPED_TRANCEIVER(bit32, bit[32])

// define the sender interface for data type 'bit[32]'
DEFINE_I_TYPED_SENDER(bit32, bit[32])

// define the receiver interface for data type 'bit[32]'
DEFINE_I_TYPED_RECEIVER(bit32, bit[32])


// define the queue channel for data type 'bit[32]'
DEFINE_C_TYPED_QUEUE(bit32, bit[32])


// EOF c_bit32_queue.sc
