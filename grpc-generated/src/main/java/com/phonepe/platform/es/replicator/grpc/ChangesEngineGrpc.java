package com.phonepe.platform.es.replicator.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.49.0)",
    comments = "Source: engine.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ChangesEngineGrpc {

  private ChangesEngineGrpc() {}

  public static final String SERVICE_NAME = "ChangesEngine";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse> getGetShardChangesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetShardChanges",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse> getGetShardChangesMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse> getGetShardChangesMethod;
    if ((getGetShardChangesMethod = ChangesEngineGrpc.getGetShardChangesMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getGetShardChangesMethod = ChangesEngineGrpc.getGetShardChangesMethod) == null) {
          ChangesEngineGrpc.getGetShardChangesMethod = getGetShardChangesMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetShardChanges"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("GetShardChanges"))
              .build();
        }
      }
    }
    return getGetShardChangesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse> getGetIndexAndShardsMetadataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetIndexAndShardsMetadata",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse> getGetIndexAndShardsMetadataMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse> getGetIndexAndShardsMetadataMethod;
    if ((getGetIndexAndShardsMetadataMethod = ChangesEngineGrpc.getGetIndexAndShardsMetadataMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getGetIndexAndShardsMetadataMethod = ChangesEngineGrpc.getGetIndexAndShardsMetadataMethod) == null) {
          ChangesEngineGrpc.getGetIndexAndShardsMetadataMethod = getGetIndexAndShardsMetadataMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetIndexAndShardsMetadata"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("GetIndexAndShardsMetadata"))
              .build();
        }
      }
    }
    return getGetIndexAndShardsMetadataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse> getApplyTranslogMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ApplyTranslog",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse> getApplyTranslogMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest, com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse> getApplyTranslogMethod;
    if ((getApplyTranslogMethod = ChangesEngineGrpc.getApplyTranslogMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getApplyTranslogMethod = ChangesEngineGrpc.getApplyTranslogMethod) == null) {
          ChangesEngineGrpc.getApplyTranslogMethod = getApplyTranslogMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest, com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ApplyTranslog"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("ApplyTranslog"))
              .build();
        }
      }
    }
    return getApplyTranslogMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse> getGetIndexMetadataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetIndexMetadata",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse> getGetIndexMetadataMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse> getGetIndexMetadataMethod;
    if ((getGetIndexMetadataMethod = ChangesEngineGrpc.getGetIndexMetadataMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getGetIndexMetadataMethod = ChangesEngineGrpc.getGetIndexMetadataMethod) == null) {
          ChangesEngineGrpc.getGetIndexMetadataMethod = getGetIndexMetadataMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetIndexMetadata"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("GetIndexMetadata"))
              .build();
        }
      }
    }
    return getGetIndexMetadataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse> getCreateIndexMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateIndex",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse> getCreateIndexMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest, com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse> getCreateIndexMethod;
    if ((getCreateIndexMethod = ChangesEngineGrpc.getCreateIndexMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getCreateIndexMethod = ChangesEngineGrpc.getCreateIndexMethod) == null) {
          ChangesEngineGrpc.getCreateIndexMethod = getCreateIndexMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest, com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateIndex"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("CreateIndex"))
              .build();
        }
      }
    }
    return getCreateIndexMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse> getSyncMappingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SyncMapping",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse> getSyncMappingMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest, com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse> getSyncMappingMethod;
    if ((getSyncMappingMethod = ChangesEngineGrpc.getSyncMappingMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getSyncMappingMethod = ChangesEngineGrpc.getSyncMappingMethod) == null) {
          ChangesEngineGrpc.getSyncMappingMethod = getSyncMappingMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest, com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SyncMapping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("SyncMapping"))
              .build();
        }
      }
    }
    return getSyncMappingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse> getGetMappingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMapping",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse> getGetMappingMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse> getGetMappingMethod;
    if ((getGetMappingMethod = ChangesEngineGrpc.getGetMappingMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getGetMappingMethod = ChangesEngineGrpc.getGetMappingMethod) == null) {
          ChangesEngineGrpc.getGetMappingMethod = getGetMappingMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest, com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMapping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("GetMapping"))
              .build();
        }
      }
    }
    return getGetMappingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse> getCloseIndicesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CloseIndices",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse> getCloseIndicesMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest, com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse> getCloseIndicesMethod;
    if ((getCloseIndicesMethod = ChangesEngineGrpc.getCloseIndicesMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getCloseIndicesMethod = ChangesEngineGrpc.getCloseIndicesMethod) == null) {
          ChangesEngineGrpc.getCloseIndicesMethod = getCloseIndicesMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest, com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CloseIndices"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("CloseIndices"))
              .build();
        }
      }
    }
    return getCloseIndicesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse> getOpenIndicesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OpenIndices",
      requestType = com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest.class,
      responseType = com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest,
      com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse> getOpenIndicesMethod() {
    io.grpc.MethodDescriptor<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest, com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse> getOpenIndicesMethod;
    if ((getOpenIndicesMethod = ChangesEngineGrpc.getOpenIndicesMethod) == null) {
      synchronized (ChangesEngineGrpc.class) {
        if ((getOpenIndicesMethod = ChangesEngineGrpc.getOpenIndicesMethod) == null) {
          ChangesEngineGrpc.getOpenIndicesMethod = getOpenIndicesMethod =
              io.grpc.MethodDescriptor.<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest, com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "OpenIndices"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ChangesEngineMethodDescriptorSupplier("OpenIndices"))
              .build();
        }
      }
    }
    return getOpenIndicesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChangesEngineStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChangesEngineStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChangesEngineStub>() {
        @java.lang.Override
        public ChangesEngineStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChangesEngineStub(channel, callOptions);
        }
      };
    return ChangesEngineStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChangesEngineBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChangesEngineBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChangesEngineBlockingStub>() {
        @java.lang.Override
        public ChangesEngineBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChangesEngineBlockingStub(channel, callOptions);
        }
      };
    return ChangesEngineBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChangesEngineFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChangesEngineFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChangesEngineFutureStub>() {
        @java.lang.Override
        public ChangesEngineFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChangesEngineFutureStub(channel, callOptions);
        }
      };
    return ChangesEngineFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ChangesEngineImplBase implements io.grpc.BindableService {

    /**
     */
    public void getShardChanges(com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetShardChangesMethod(), responseObserver);
    }

    /**
     */
    public void getIndexAndShardsMetadata(com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetIndexAndShardsMetadataMethod(), responseObserver);
    }

    /**
     */
    public void applyTranslog(com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getApplyTranslogMethod(), responseObserver);
    }

    /**
     */
    public void getIndexMetadata(com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetIndexMetadataMethod(), responseObserver);
    }

    /**
     */
    public void createIndex(com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateIndexMethod(), responseObserver);
    }

    /**
     */
    public void syncMapping(com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSyncMappingMethod(), responseObserver);
    }

    /**
     */
    public void getMapping(com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMappingMethod(), responseObserver);
    }

    /**
     */
    public void closeIndices(com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCloseIndicesMethod(), responseObserver);
    }

    /**
     */
    public void openIndices(com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOpenIndicesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetShardChangesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse>(
                  this, METHODID_GET_SHARD_CHANGES)))
          .addMethod(
            getGetIndexAndShardsMetadataMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse>(
                  this, METHODID_GET_INDEX_AND_SHARDS_METADATA)))
          .addMethod(
            getApplyTranslogMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse>(
                  this, METHODID_APPLY_TRANSLOG)))
          .addMethod(
            getGetIndexMetadataMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse>(
                  this, METHODID_GET_INDEX_METADATA)))
          .addMethod(
            getCreateIndexMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse>(
                  this, METHODID_CREATE_INDEX)))
          .addMethod(
            getSyncMappingMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse>(
                  this, METHODID_SYNC_MAPPING)))
          .addMethod(
            getGetMappingMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse>(
                  this, METHODID_GET_MAPPING)))
          .addMethod(
            getCloseIndicesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse>(
                  this, METHODID_CLOSE_INDICES)))
          .addMethod(
            getOpenIndicesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest,
                com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse>(
                  this, METHODID_OPEN_INDICES)))
          .build();
    }
  }

  /**
   */
  public static final class ChangesEngineStub extends io.grpc.stub.AbstractAsyncStub<ChangesEngineStub> {
    private ChangesEngineStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChangesEngineStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChangesEngineStub(channel, callOptions);
    }

    /**
     */
    public void getShardChanges(com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetShardChangesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getIndexAndShardsMetadata(com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetIndexAndShardsMetadataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void applyTranslog(com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getApplyTranslogMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getIndexMetadata(com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetIndexMetadataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createIndex(com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateIndexMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void syncMapping(com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSyncMappingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMapping(com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMappingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeIndices(com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCloseIndicesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void openIndices(com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest request,
        io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOpenIndicesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ChangesEngineBlockingStub extends io.grpc.stub.AbstractBlockingStub<ChangesEngineBlockingStub> {
    private ChangesEngineBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChangesEngineBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChangesEngineBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse getShardChanges(com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetShardChangesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse getIndexAndShardsMetadata(com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetIndexAndShardsMetadataMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse applyTranslog(com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getApplyTranslogMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse getIndexMetadata(com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetIndexMetadataMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse createIndex(com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateIndexMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse syncMapping(com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSyncMappingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse getMapping(com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMappingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse closeIndices(com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCloseIndicesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse openIndices(com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOpenIndicesMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ChangesEngineFutureStub extends io.grpc.stub.AbstractFutureStub<ChangesEngineFutureStub> {
    private ChangesEngineFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChangesEngineFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChangesEngineFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse> getShardChanges(
        com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetShardChangesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse> getIndexAndShardsMetadata(
        com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetIndexAndShardsMetadataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse> applyTranslog(
        com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getApplyTranslogMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse> getIndexMetadata(
        com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetIndexMetadataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse> createIndex(
        com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateIndexMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse> syncMapping(
        com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSyncMappingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse> getMapping(
        com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMappingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse> closeIndices(
        com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCloseIndicesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse> openIndices(
        com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOpenIndicesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SHARD_CHANGES = 0;
  private static final int METHODID_GET_INDEX_AND_SHARDS_METADATA = 1;
  private static final int METHODID_APPLY_TRANSLOG = 2;
  private static final int METHODID_GET_INDEX_METADATA = 3;
  private static final int METHODID_CREATE_INDEX = 4;
  private static final int METHODID_SYNC_MAPPING = 5;
  private static final int METHODID_GET_MAPPING = 6;
  private static final int METHODID_CLOSE_INDICES = 7;
  private static final int METHODID_OPEN_INDICES = 8;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ChangesEngineImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ChangesEngineImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SHARD_CHANGES:
          serviceImpl.getShardChanges((com.phonepe.platform.es.replicator.grpc.Engine.GetChangesRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetChangesResponse>) responseObserver);
          break;
        case METHODID_GET_INDEX_AND_SHARDS_METADATA:
          serviceImpl.getIndexAndShardsMetadata((com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexAndShardsMetadataResponse>) responseObserver);
          break;
        case METHODID_APPLY_TRANSLOG:
          serviceImpl.applyTranslog((com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ApplyTranslogResponse>) responseObserver);
          break;
        case METHODID_GET_INDEX_METADATA:
          serviceImpl.getIndexMetadata((com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetIndexMetadataResponse>) responseObserver);
          break;
        case METHODID_CREATE_INDEX:
          serviceImpl.createIndex((com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ESCreateIndexResponse>) responseObserver);
          break;
        case METHODID_SYNC_MAPPING:
          serviceImpl.syncMapping((com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.ApplyMappingResponse>) responseObserver);
          break;
        case METHODID_GET_MAPPING:
          serviceImpl.getMapping((com.phonepe.platform.es.replicator.grpc.Engine.GetMappingRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.GetMappingResponse>) responseObserver);
          break;
        case METHODID_CLOSE_INDICES:
          serviceImpl.closeIndices((com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.CloseIndicesResponse>) responseObserver);
          break;
        case METHODID_OPEN_INDICES:
          serviceImpl.openIndices((com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesRequest) request,
              (io.grpc.stub.StreamObserver<com.phonepe.platform.es.replicator.grpc.Engine.OpenIndicesResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ChangesEngineBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChangesEngineBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.phonepe.platform.es.replicator.grpc.Engine.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ChangesEngine");
    }
  }

  private static final class ChangesEngineFileDescriptorSupplier
      extends ChangesEngineBaseDescriptorSupplier {
    ChangesEngineFileDescriptorSupplier() {}
  }

  private static final class ChangesEngineMethodDescriptorSupplier
      extends ChangesEngineBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ChangesEngineMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ChangesEngineGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChangesEngineFileDescriptorSupplier())
              .addMethod(getGetShardChangesMethod())
              .addMethod(getGetIndexAndShardsMetadataMethod())
              .addMethod(getApplyTranslogMethod())
              .addMethod(getGetIndexMetadataMethod())
              .addMethod(getCreateIndexMethod())
              .addMethod(getSyncMappingMethod())
              .addMethod(getGetMappingMethod())
              .addMethod(getCloseIndicesMethod())
              .addMethod(getOpenIndicesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
