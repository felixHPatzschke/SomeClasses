/*
 * @author Felix
 */

#include "Vector.h"
#include "Integration.h"


namespace phs
{

	unsigned int integration_getPrecisionMultiplicator(unsigned int basic, Vector* v, Vector* a)
	{
		double res = basic*(1+v->abs())*(1+a->abs());
		return ((unsigned int) res);
	}

	void integrate_euler(Vector* s, Vector* v, Vector (*a)(Vector s), double dt, unsigned int basicPrecision)
	{
		unsigned int precision = phs::integration_getPrecisionMultiplicator(basicPrecision, v, (&a(*s)));
		dt /= precision;
		for(unsigned int c=0; c<precision; c++)
		{
			Vector v2 = v->operator+((a(*s))*dt);
			s->operator+=(( ((*v)*dt)+v2 )*0.5);
			//std::cout << s->debug("s_n:") << std::endl;
			//v = &v2;
			*v = v2;
		}
	}

}