package de.tum;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.0)",
    comments = "Source: ECS.proto")
public class ServiceGrpc {

  private ServiceGrpc() {}

  public static final String SERVICE_NAME = "Service";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<de.tum.ServiceProto.ServiceRequest,
      de.tum.ServiceProto.ServiceResponse> METHOD_CALL =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "Service", "Call"),
          io.grpc.protobuf.ProtoUtils.marshaller(de.tum.ServiceProto.ServiceRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(de.tum.ServiceProto.ServiceResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceStub newStub(io.grpc.Channel channel) {
    return new ServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void call(de.tum.ServiceProto.ServiceRequest request,
        io.grpc.stub.StreamObserver<de.tum.ServiceProto.ServiceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CALL, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CALL,
            asyncUnaryCall(
              new MethodHandlers<
                de.tum.ServiceProto.ServiceRequest,
                de.tum.ServiceProto.ServiceResponse>(
                  this, METHODID_CALL)))
          .build();
    }
  }

  /**
   */
  public static final class ServiceStub extends io.grpc.stub.AbstractStub<ServiceStub> {
    private ServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServiceStub(channel, callOptions);
    }

    /**
     */
    public void call(de.tum.ServiceProto.ServiceRequest request,
        io.grpc.stub.StreamObserver<de.tum.ServiceProto.ServiceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CALL, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ServiceBlockingStub extends io.grpc.stub.AbstractStub<ServiceBlockingStub> {
    private ServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public de.tum.ServiceProto.ServiceResponse call(de.tum.ServiceProto.ServiceRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CALL, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ServiceFutureStub extends io.grpc.stub.AbstractStub<ServiceFutureStub> {
    private ServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.ServiceProto.ServiceResponse> call(
        de.tum.ServiceProto.ServiceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CALL, getCallOptions()), request);
    }
  }

  private static final int METHODID_CALL = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(ServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CALL:
          serviceImpl.call((de.tum.ServiceProto.ServiceRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.ServiceProto.ServiceResponse>) responseObserver);
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

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_CALL);
  }

}
