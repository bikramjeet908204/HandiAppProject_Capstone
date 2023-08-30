package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the location type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "locations", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class location implements Model {
  public static final QueryField ID = field("location", "id");
  public static final QueryField ADDRESS = field("location", "address");
  public static final QueryField TITLE = field("location", "title");
  public static final QueryField LATLONG = field("location", "latlong");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String address;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String", isRequired = true) String latlong;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getAddress() {
      return address;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getLatlong() {
      return latlong;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private location(String id, String address, String title, String latlong) {
    this.id = id;
    this.address = address;
    this.title = title;
    this.latlong = latlong;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      location location = (location) obj;
      return ObjectsCompat.equals(getId(), location.getId()) &&
              ObjectsCompat.equals(getAddress(), location.getAddress()) &&
              ObjectsCompat.equals(getTitle(), location.getTitle()) &&
              ObjectsCompat.equals(getLatlong(), location.getLatlong()) &&
              ObjectsCompat.equals(getCreatedAt(), location.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), location.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getAddress())
      .append(getTitle())
      .append(getLatlong())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("location {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("address=" + String.valueOf(getAddress()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("latlong=" + String.valueOf(getLatlong()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static AddressStep builder() {
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
  public static location justId(String id) {
    return new location(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      address,
      title,
      latlong);
  }
  public interface AddressStep {
    TitleStep address(String address);
  }
  

  public interface TitleStep {
    LatlongStep title(String title);
  }
  

  public interface LatlongStep {
    BuildStep latlong(String latlong);
  }
  

  public interface BuildStep {
    location build();
    BuildStep id(String id);
  }
  

  public static class Builder implements AddressStep, TitleStep, LatlongStep, BuildStep {
    private String id;
    private String address;
    private String title;
    private String latlong;
    @Override
     public location build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new location(
          id,
          address,
          title,
          latlong);
    }
    
    @Override
     public TitleStep address(String address) {
        Objects.requireNonNull(address);
        this.address = address;
        return this;
    }
    
    @Override
     public LatlongStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep latlong(String latlong) {
        Objects.requireNonNull(latlong);
        this.latlong = latlong;
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
    private CopyOfBuilder(String id, String address, String title, String latlong) {
      super.id(id);
      super.address(address)
        .title(title)
        .latlong(latlong);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder latlong(String latlong) {
      return (CopyOfBuilder) super.latlong(latlong);
    }
  }
  
}
