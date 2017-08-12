package org.interledger.cryptoconditions;

import org.interledger.cryptoconditions.der.DerEncodingException;

/**
 * This class provides shared, concrete logic for all conditions.
 */
public abstract class ConditionBase implements Condition {

  private final long cost;

  /**
   * Default internal constructor for all conditions. Sub-classes must statically calculate the cost
   * of a condition and call this constructor with the correct cost value.
   *
   * @param cost the cost value for this condition.
   */
  protected ConditionBase(final long cost) {
    if (cost < 0) {
      throw new IllegalArgumentException("Cost must be positive!");
    }

    this.cost = cost;
  }

  @Override
  public final long getCost() {
    return cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConditionBase that = (ConditionBase) o;

    return cost == that.cost;
  }

  @Override
  public int hashCode() {
    return (int) (cost ^ (cost >>> 32));
  }

  /**
   * Overrides the default {@link java.lang.Object#toString()} and returns the result of
   * {@link CryptoConditionUri#toUri(Condition)} as a string.
   */
  @Override
  public final String toString() {
    return CryptoConditionUri.toUri(this).toString();
  }

  /**
   * An implementation of {@link Comparable#compareTo(Object)} to conform to the {@link Comparable}
   * interface.
   *
   * This implementation merely loops through the bytes of each encoded condition and returns the
   * result of that comparison.
   *
   * @param that A {@link Condition} to compare against this condition.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   * or greater than the specified object.
   */
  @Override
  public int compareTo(Condition that) {
    try {
      byte[] c1encoded = CryptoConditionWriter.writeCondition(this);
      byte[] c2encoded = CryptoConditionWriter.writeCondition(that);

      int minLength = Math.min(c1encoded.length, c2encoded.length);
      for (int i = 0; i < minLength; i++) {
        int result = Integer.compareUnsigned(c1encoded[i], c2encoded[i]);
        if (result != 0) {
          return result;
        }
      }
      return c1encoded.length - c2encoded.length;

    } catch (DerEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
