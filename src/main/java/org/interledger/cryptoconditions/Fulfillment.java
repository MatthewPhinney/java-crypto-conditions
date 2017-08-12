package org.interledger.cryptoconditions;

/**
 * An implementation of a crypto-conditions Fulfillment.
 *
 * @see "https://datatracker.ietf.org/doc/draft-thomas-crypto-conditions/"
 */
public interface Fulfillment<C extends Condition> extends Comparable<Fulfillment<C>> {

  /**
   * Accessor for the type of this fulfillment.
   *
   * @return A {@link CryptoConditionType} for this fulfillment.
   */
  CryptoConditionType getType();

  /**
   * Accessor for a copy of the bytes that encode this {@link Fulfillment}.
   *
   * @return A {@link byte[]} that provides access to the binary encoding of this {@link Fulfillment}.
   */
  byte[] getEncoded();

  /**
   * Accessor for an immutable, read-only view of the bytes that encode this {@link Fulfillment}, as
   * a Base64 encoded {@link String}.
   *
   * @return A read-only {@link byte[]} that provides access to the binary encoding of this
   * {@link Fulfillment}.
   */
  String getEncodedBase64();

  /**
   * Accessor for the condition that corresponds to this fulfillment.
   *
   * @return A {@link Condition} that can be fulfilled by this fulfillment.
   */
  C getCondition();

  /**
   * Verify that this fulfillment validates the supplied {@code condition}. A fulfillment is
   * validated by evaluating that the circuit output is {@code true} but also that the provided
   * fulfillment matches the circuit fingerprint, which is the  {@code condition.
   *
   * @param condition A {@link Condition} that this fulfillment should validate.
   * @param message   A {@link byte[]} that is part of verifying the supplied condition.
   * @return {@code true} if this fulfillment validates the supplied condition and message; {@code
   * false} otherwise.
   */
  boolean verify(C condition, byte[] message);

}
