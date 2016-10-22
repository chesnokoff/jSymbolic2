package jsymbolic2.features;

import java.util.LinkedList;
import javax.sound.midi.*;
import ace.datatypes.FeatureDefinition;
import jsymbolic2.featureutils.MIDIFeatureExtractor;
import jsymbolic2.processing.MIDIIntermediateRepresentations;

/**
 * A feature calculator that is set to 1 if the initial meter is compound (numerator of time signature is
 * greater than or equal to 6 and is evenly divisible by 3) and to 0 if it is simple (if the above condition
 * is not fulfilled).
 *
 * @author Cory McKay
 */
public class CompoundOrSimpleMeterFeature
		extends MIDIFeatureExtractor
{
	/* CONSTRUCTOR ******************************************************************************************/

	
	/**
	 * Basic constructor that sets the values of the fields inherited from this class' superclass.
	 */
	public CompoundOrSimpleMeterFeature()
	{
		code = "R-32";
		String name = "Compound Or Simple Meter";
		String description = "Set to 1 if the initial meter is compound (numerator of time signature is greater than or equal to 6 and is evenly divisible by 3) and to 0 if it is simple (if the above condition is not fulfilled).";
		boolean is_sequential = true;
		int dimensions = 1;
		definition = new FeatureDefinition(name, description, is_sequential, dimensions);
		dependencies = null;
		offsets = null;
	}
	

	/* PUBLIC METHODS ***************************************************************************************/
	
	
	/**
	 * Extract this feature from the given sequence of MIDI data and its associated information.
	 *
	 * @param sequence				The MIDI data to extract the feature from.
	 * @param sequence_info			Additional data already extracted from the the MIDI sequence.
	 * @param other_feature_values	The values of other features that may be needed to calculate this feature. 
	 *								The order and offsets of these features must be the same as those returned
	 *								by this class' getDependencies and getDependencyOffsets methods, 
	 *								respectively. The first indice indicates the feature/window, and the 
	 *								second indicates the value.
	 * @return						The extracted feature value(s).
	 * @throws Exception			Throws an informative exception if the feature cannot be calculated.
	 */
	@Override
	public double[] extractFeature( Sequence sequence,
									MIDIIntermediateRepresentations sequence_info,
									double[][] other_feature_values )
	throws Exception
	{
		double value = 0.0;
		if (sequence_info != null)
		{
			// Default to simple
			value = 0.0;

			// If time signature specified
			if (!((LinkedList) sequence_info.overall_metadata[1]).isEmpty())
			{
				// Convert data types
				Object[] numerators_objects = ((LinkedList) sequence_info.overall_metadata[1]).toArray();
				int[] numerators = new int[numerators_objects.length];
				for (int i = 0; i < numerators.length; i++)
					numerators[i] = ((Integer) numerators_objects[i]).intValue();

				// Find initial numerator
				int num = numerators[0];

				// Find if compound
				if (num >= 6 && (num % 3) == 0)
					value = 1.0;
			}
		}

		double[] result = new double[1];
		result[0] = value;
		return result;
	}
}