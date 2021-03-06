// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dsp-campaign-cost.proto

package com.emarbox.dsp.api.domain;

public final class DspCampaignCost {
  private DspCampaignCost() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DspCampaignCostInfoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 campaignId = 1;
    boolean hasCampaignId();
    int getCampaignId();
    
    // optional double todayCost = 2;
    boolean hasTodayCost();
    double getTodayCost();
    
    // optional int32 todayTotalTime = 3;
    boolean hasTodayTotalTime();
    int getTodayTotalTime();
    
    // optional int32 todayRemainTime = 4;
    boolean hasTodayRemainTime();
    int getTodayRemainTime();
    
    // optional string date = 5;
    boolean hasDate();
    String getDate();
  }
  public static final class DspCampaignCostInfo extends
      com.google.protobuf.GeneratedMessage
      implements DspCampaignCostInfoOrBuilder {
    // Use DspCampaignCostInfo.newBuilder() to construct.
    private DspCampaignCostInfo(Builder builder) {
      super(builder);
    }
    private DspCampaignCostInfo(boolean noInit) {}
    
    private static final DspCampaignCostInfo defaultInstance;
    public static DspCampaignCostInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public DspCampaignCostInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.emarbox.dsp.api.domain.DspCampaignCost.internal_static_DspCampaignCostInfo_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.emarbox.dsp.api.domain.DspCampaignCost.internal_static_DspCampaignCostInfo_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int32 campaignId = 1;
    public static final int CAMPAIGNID_FIELD_NUMBER = 1;
    private int campaignId_;
    public boolean hasCampaignId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getCampaignId() {
      return campaignId_;
    }
    
    // optional double todayCost = 2;
    public static final int TODAYCOST_FIELD_NUMBER = 2;
    private double todayCost_;
    public boolean hasTodayCost() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public double getTodayCost() {
      return todayCost_;
    }
    
    // optional int32 todayTotalTime = 3;
    public static final int TODAYTOTALTIME_FIELD_NUMBER = 3;
    private int todayTotalTime_;
    public boolean hasTodayTotalTime() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getTodayTotalTime() {
      return todayTotalTime_;
    }
    
    // optional int32 todayRemainTime = 4;
    public static final int TODAYREMAINTIME_FIELD_NUMBER = 4;
    private int todayRemainTime_;
    public boolean hasTodayRemainTime() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getTodayRemainTime() {
      return todayRemainTime_;
    }
    
    // optional string date = 5;
    public static final int DATE_FIELD_NUMBER = 5;
    private java.lang.Object date_;
    public boolean hasDate() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getDate() {
      java.lang.Object ref = date_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          date_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getDateBytes() {
      java.lang.Object ref = date_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        date_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      campaignId_ = 0;
      todayCost_ = 0D;
      todayTotalTime_ = 0;
      todayRemainTime_ = 0;
      date_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasCampaignId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, campaignId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeDouble(2, todayCost_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, todayTotalTime_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, todayRemainTime_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getDateBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, campaignId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(2, todayCost_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, todayTotalTime_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, todayRemainTime_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getDateBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.emarbox.dsp.api.domain.DspCampaignCost.internal_static_DspCampaignCostInfo_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.emarbox.dsp.api.domain.DspCampaignCost.internal_static_DspCampaignCostInfo_fieldAccessorTable;
      }
      
      // Construct using com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        campaignId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        todayCost_ = 0D;
        bitField0_ = (bitField0_ & ~0x00000002);
        todayTotalTime_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        todayRemainTime_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        date_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo.getDescriptor();
      }
      
      public com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo getDefaultInstanceForType() {
        return com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo.getDefaultInstance();
      }
      
      public com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo build() {
        com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo buildPartial() {
        com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo result = new com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.campaignId_ = campaignId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.todayCost_ = todayCost_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.todayTotalTime_ = todayTotalTime_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.todayRemainTime_ = todayRemainTime_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.date_ = date_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo) {
          return mergeFrom((com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo other) {
        if (other == com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo.getDefaultInstance()) return this;
        if (other.hasCampaignId()) {
          setCampaignId(other.getCampaignId());
        }
        if (other.hasTodayCost()) {
          setTodayCost(other.getTodayCost());
        }
        if (other.hasTodayTotalTime()) {
          setTodayTotalTime(other.getTodayTotalTime());
        }
        if (other.hasTodayRemainTime()) {
          setTodayRemainTime(other.getTodayRemainTime());
        }
        if (other.hasDate()) {
          setDate(other.getDate());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasCampaignId()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              campaignId_ = input.readInt32();
              break;
            }
            case 17: {
              bitField0_ |= 0x00000002;
              todayCost_ = input.readDouble();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              todayTotalTime_ = input.readInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              todayRemainTime_ = input.readInt32();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              date_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 campaignId = 1;
      private int campaignId_ ;
      public boolean hasCampaignId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getCampaignId() {
        return campaignId_;
      }
      public Builder setCampaignId(int value) {
        bitField0_ |= 0x00000001;
        campaignId_ = value;
        onChanged();
        return this;
      }
      public Builder clearCampaignId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        campaignId_ = 0;
        onChanged();
        return this;
      }
      
      // optional double todayCost = 2;
      private double todayCost_ ;
      public boolean hasTodayCost() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public double getTodayCost() {
        return todayCost_;
      }
      public Builder setTodayCost(double value) {
        bitField0_ |= 0x00000002;
        todayCost_ = value;
        onChanged();
        return this;
      }
      public Builder clearTodayCost() {
        bitField0_ = (bitField0_ & ~0x00000002);
        todayCost_ = 0D;
        onChanged();
        return this;
      }
      
      // optional int32 todayTotalTime = 3;
      private int todayTotalTime_ ;
      public boolean hasTodayTotalTime() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getTodayTotalTime() {
        return todayTotalTime_;
      }
      public Builder setTodayTotalTime(int value) {
        bitField0_ |= 0x00000004;
        todayTotalTime_ = value;
        onChanged();
        return this;
      }
      public Builder clearTodayTotalTime() {
        bitField0_ = (bitField0_ & ~0x00000004);
        todayTotalTime_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 todayRemainTime = 4;
      private int todayRemainTime_ ;
      public boolean hasTodayRemainTime() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public int getTodayRemainTime() {
        return todayRemainTime_;
      }
      public Builder setTodayRemainTime(int value) {
        bitField0_ |= 0x00000008;
        todayRemainTime_ = value;
        onChanged();
        return this;
      }
      public Builder clearTodayRemainTime() {
        bitField0_ = (bitField0_ & ~0x00000008);
        todayRemainTime_ = 0;
        onChanged();
        return this;
      }
      
      // optional string date = 5;
      private java.lang.Object date_ = "";
      public boolean hasDate() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getDate() {
        java.lang.Object ref = date_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          date_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setDate(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        date_ = value;
        onChanged();
        return this;
      }
      public Builder clearDate() {
        bitField0_ = (bitField0_ & ~0x00000010);
        date_ = getDefaultInstance().getDate();
        onChanged();
        return this;
      }
      void setDate(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        date_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:DspCampaignCostInfo)
    }
    
    static {
      defaultInstance = new DspCampaignCostInfo(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DspCampaignCostInfo)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_DspCampaignCostInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DspCampaignCostInfo_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027dsp-campaign-cost.proto\"{\n\023DspCampaign" +
      "CostInfo\022\022\n\ncampaignId\030\001 \002(\005\022\021\n\ttodayCos" +
      "t\030\002 \001(\001\022\026\n\016todayTotalTime\030\003 \001(\005\022\027\n\017today" +
      "RemainTime\030\004 \001(\005\022\014\n\004date\030\005 \001(\tB-\n\032com.em" +
      "arbox.dsp.api.domainB\017DspCampaignCost"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_DspCampaignCostInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_DspCampaignCostInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DspCampaignCostInfo_descriptor,
              new java.lang.String[] { "CampaignId", "TodayCost", "TodayTotalTime", "TodayRemainTime", "Date", },
              com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo.class,
              com.emarbox.dsp.api.domain.DspCampaignCost.DspCampaignCostInfo.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
