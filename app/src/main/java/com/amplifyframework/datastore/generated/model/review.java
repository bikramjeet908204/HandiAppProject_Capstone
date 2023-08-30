package com.amplifyframework.datastore.generated.model;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.Objects;
import java.util.UUID;

/** This is an auto generated class representing the review type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "reviews", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class review implements Model {
  public static final QueryField ID = field("review", "id");
  public static final QueryField USERNAME = field("review", "username");
  public static final QueryField LOCATION_TITLE = field("review", "locationTitle");
  public static final QueryField LOCATION_ADDRESS = field("review", "locationAddress");
  public static final QueryField DESCRIPTION = field("review", "description");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String username;
  private final @ModelField(targetType="String", isRequired = true) String locationTitle;
  private final @ModelField(targetType="String", isRequired = true) String locationAddress;
  private final @ModelField(targetType="String", isRequired = true) String description;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUsername() {
      return username;
  }
  
  public String getLocationTitle() {
      return locationTitle;
  }
  
  public String getLocationAddress() {
      return locationAddress;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private review(String id, String username, String locationTitle, String locationAddress, String description) {
    this.id = id;
    this.username = username;
    this.locationTitle = locationTitle;
    this.locationAddress = locationAddress;
    this.description = description;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      review review = (review) obj;
      return ObjectsCompat.equals(getId(), review.getId()) &&
              ObjectsCompat.equals(getUsername(), review.getUsername()) &&
              ObjectsCompat.equals(getLocationTitle(), review.getLocationTitle()) &&
              ObjectsCompat.equals(getLocationAddress(), review.getLocationAddress()) &&
              ObjectsCompat.equals(getDescription(), review.getDescription()) &&
              ObjectsCompat.equals(getCreatedAt(), review.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), review.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUsername())
      .append(getLocationTitle())
      .append(getLocationAddress())
      .append(getDescription())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("review {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("locationTitle=" + String.valueOf(getLocationTitle()) + ", ")
      .append("locationAddress=" + String.valueOf(getLocationAddress()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UsernameStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static review justId(String id) {
    return new review(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      username,
      locationTitle,
      locationAddress,
      description);
  }
  public interface UsernameStep {
    LocationTitleStep username(String username);
  }
  

  public interface LocationTitleStep {
    LocationAddressStep locationTitle(String locationTitle);
  }
  

  public interface LocationAddressStep {
    DescriptionStep locationAddress(String locationAddress);
  }
  

  public interface DescriptionStep {
    BuildStep description(String description);
  }
  

  public interface BuildStep {
    review build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UsernameStep, LocationTitleStep, LocationAddressStep, DescriptionStep, BuildStep {
    private String id;
    private String username;
    private String locationTitle;
    private String locationAddress;
    private String description;
    @Override
     public review build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new review(
          id,
          username,
          locationTitle,
          locationAddress,
          description);
    }
    
    @Override
     public LocationTitleStep username(String username) {
        Objects.requireNonNull(username);
        this.username = username;
        return this;
    }
    
    @Override
     public LocationAddressStep locationTitle(String locationTitle) {
        Objects.requireNonNull(locationTitle);
        this.locationTitle = locationTitle;
        return this;
    }
    
    @Override
     public DescriptionStep locationAddress(String locationAddress) {
        Objects.requireNonNull(locationAddress);
        this.locationAddress = locationAddress;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        Objects.requireNonNull(description);
        this.description = description;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String username, String locationTitle, String locationAddress, String description) {
      super.id(id);
      super.username(username)
        .locationTitle(locationTitle)
        .locationAddress(locationAddress)
        .description(description);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder locationTitle(String locationTitle) {
      return (CopyOfBuilder) super.locationTitle(locationTitle);
    }
    
    @Override
     public CopyOfBuilder locationAddress(String locationAddress) {
      return (CopyOfBuilder) super.locationAddress(locationAddress);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
  }
  
}
