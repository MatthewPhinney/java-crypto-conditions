package org.interledger.cryptoconditions.types;

import java.util.Arrays;
import org.interledger.cryptoconditions.Condition;
import org.interledger.cryptoconditions.ConditionType;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.cryptoconditions.der.DerOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Implementation of a fulfillment based on a preimage and the SHA-256 function.
 */
public class PreimageSha256Fulfillment implements Fulfillment {

  private PreimageSha256Condition condition;
  private byte[] preimage;

  /**
   * Constructs an instance of the fulfillment.
   * 
   * @param preimage The preimage associated with the fulfillment.
   */
  public PreimageSha256Fulfillment(byte[] preimage) {

    this.preimage = Arrays.copyOf(preimage, preimage.length);
//    this.preimage = new byte[preimage.length];
//    System.arraycopy(preimage, 0, this.preimage, 0, preimage.length);
  }

  @Override
  public ConditionType getType() {
    return ConditionType.PREIMAGE_SHA256;
  }

  /**
   * Returns a copy of the preimage associated with the fulfillment.
   */
  public byte[] getPreimage() {
    byte[] preimage = new byte[this.preimage.length];
    System.arraycopy(this.preimage, 0, preimage, 0, this.preimage.length);
    return preimage;
  }

  @Override
  public byte[] getEncoded() {
    try {
      // Build preimage sequence
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DerOutputStream out = new DerOutputStream(baos);
      out.writeTaggedObject(0, preimage);
      out.close();
      byte[] buffer = baos.toByteArray();

      // Wrap CHOICE
      baos = new ByteArrayOutputStream();
      out = new DerOutputStream(baos);
      out.writeTaggedConstructedObject(getType().getTypeCode(), buffer);
      out.close();

      return baos.toByteArray();

    } catch (IOException e) {
      throw new UncheckedIOException("DER Encoding Error", e);
    }
  }

  @Override
  public PreimageSha256Condition getCondition() {
    if (condition == null) {
      condition = new PreimageSha256Condition(preimage);
    }
    return condition;
  }

  @Override
  public boolean verify(Condition condition, byte[] message) {

    if (condition == null) {
      throw new IllegalArgumentException(
          "Can't verify a PreimageSha256Fulfillment against an null condition.");
    }

    if (!(condition instanceof PreimageSha256Condition)) {
      throw new IllegalArgumentException(
          "Must verify a PreimageSha256Fulfillment against PreimageSha256Condition.");
    }

    return getCondition().equals(condition);
  }

  /**
   * The {@link #condition} field in this class is not part of this equals method because it is a
   * value derived from this fulfillment, and is lazily initialized (so it's occasionally null until
   * {@link #getCondition()} is called.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PreimageSha256Fulfillment that = (PreimageSha256Fulfillment) o;

    return Arrays.equals(preimage, that.preimage);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(preimage);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PreimageSha256Fulfillment{");
    sb.append("type=").append(getType());
    sb.append('}');
    return sb.toString();
  }
}
