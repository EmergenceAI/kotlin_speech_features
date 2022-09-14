#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class KSFKotlinFloatArray, KSFKotlinArray<T>, KSFFBankResult, KSFMathUtilsCompanion, KSFKotlinIntArray, KSFKotlinDoubleArray, KSFComplex, KSFKotlinFloatIterator, KSFKotlinIntIterator, KSFKotlinDoubleIterator;

@protocol KSFFFT, KSFKotlinIterator;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface KSFBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface KSFBase (KSFBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface KSFMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface KSFMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorKSFKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface KSFNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface KSFByte : KSFNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface KSFUByte : KSFNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface KSFShort : KSFNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface KSFUShort : KSFNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface KSFInt : KSFNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface KSFUInt : KSFNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface KSFLong : KSFNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface KSFULong : KSFNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface KSFFloat : KSFNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface KSFDouble : KSFNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface KSFBoolean : KSFNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FBankResult")))
@interface KSFFBankResult : KSFBase
- (instancetype)initWithFeatures:(KSFKotlinArray<KSFKotlinFloatArray *> *)features energy:(KSFKotlinFloatArray *)energy __attribute__((swift_name("init(features:energy:)"))) __attribute__((objc_designated_initializer));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)component1 __attribute__((swift_name("component1()"))) __attribute__((deprecated("use corresponding property instead")));
- (KSFKotlinFloatArray *)component2 __attribute__((swift_name("component2()"))) __attribute__((deprecated("use corresponding property instead")));
- (KSFFBankResult *)doCopyFeatures:(KSFKotlinArray<KSFKotlinFloatArray *> *)features energy:(KSFKotlinFloatArray *)energy __attribute__((swift_name("doCopy(features:energy:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) KSFKotlinFloatArray *energy __attribute__((swift_name("energy")));
@property (readonly) KSFKotlinArray<KSFKotlinFloatArray *> *features __attribute__((swift_name("features")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MathUtils")))
@interface KSFMathUtils : KSFBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property (class, readonly, getter=companion) KSFMathUtilsCompanion *companion __attribute__((swift_name("companion")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MathUtils.Companion")))
@interface KSFMathUtilsCompanion : KSFBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) KSFMathUtilsCompanion *shared __attribute__((swift_name("shared")));
- (KSFKotlinFloatArray *)normalizeSig:(KSFKotlinIntArray *)sig __attribute__((swift_name("normalize(sig:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SignalProc")))
@interface KSFSignalProc : KSFBase
- (instancetype)initWithFft:(id<KSFFFT>)fft __attribute__((swift_name("init(fft:)"))) __attribute__((objc_designated_initializer));
- (KSFKotlinFloatArray *)deframesigFrames:(KSFKotlinArray<KSFKotlinFloatArray *> *)frames sigLen:(int32_t)sigLen frameLen:(int32_t)frameLen frameStep:(int32_t)frameStep winFunc:(KSFKotlinIntArray *(^)(KSFInt *))winFunc __attribute__((swift_name("deframesig(frames:sigLen:frameLen:frameStep:winFunc:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)framesigSignal:(KSFKotlinFloatArray *)signal frameLen:(int32_t)frameLen frameStep:(int32_t)frameStep winFunc:(KSFKotlinFloatArray * _Nullable)winFunc strideTrick:(BOOL)strideTrick __attribute__((swift_name("framesig(signal:frameLen:frameStep:winFunc:strideTrick:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)logpowspecFrames:(KSFKotlinArray<KSFKotlinFloatArray *> *)frames nfft:(int32_t)nfft norm:(BOOL)norm __attribute__((swift_name("logpowspec(frames:nfft:norm:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)magspecFrames:(KSFKotlinArray<KSFKotlinFloatArray *> *)frames nfft:(int32_t)nfft __attribute__((swift_name("magspec(frames:nfft:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)powspecFrames:(KSFKotlinArray<KSFKotlinFloatArray *> *)frames nfft:(int32_t)nfft __attribute__((swift_name("powspec(frames:nfft:)")));
- (KSFKotlinFloatArray *)preemphasisSignal:(KSFKotlinFloatArray *)signal coeff:(float)coeff __attribute__((swift_name("preemphasis(signal:coeff:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpeechFeatures")))
@interface KSFSpeechFeatures : KSFBase
- (instancetype)initWithFft:(id<KSFFFT>)fft __attribute__((swift_name("init(fft:)"))) __attribute__((objc_designated_initializer));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)deltaFeat:(KSFKotlinFloatArray *)feat n:(int32_t)n __attribute__((swift_name("delta(feat:n:)")));
- (KSFFBankResult *)fbankSignal:(KSFKotlinFloatArray *)signal sampleRate:(int32_t)sampleRate winLen:(float)winLen winStep:(float)winStep nFilt:(int32_t)nFilt nfft:(int32_t)nfft lowFreq:(int32_t)lowFreq highFreq:(KSFInt * _Nullable)highFreq preemph:(float)preemph winFunc:(KSFKotlinFloatArray * _Nullable)winFunc __attribute__((swift_name("fbank(signal:sampleRate:winLen:winStep:nFilt:nfft:lowFreq:highFreq:preemph:winFunc:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)getFilterBanksNFilt:(int32_t)nFilt nfft:(int32_t)nfft sampleRate:(int32_t)sampleRate lowFreq:(int32_t)lowFreq highFreqIn:(KSFInt * _Nullable)highFreqIn __attribute__((swift_name("getFilterBanks(nFilt:nfft:sampleRate:lowFreq:highFreqIn:)")));
- (double)hz2melHz:(double)hz __attribute__((swift_name("hz2mel(hz:)")));
- (KSFKotlinDoubleArray *)hz2melHz_:(KSFKotlinDoubleArray *)hz __attribute__((swift_name("hz2mel(hz_:)")));
- (KSFKotlinFloatArray *)lifterCepstra:(KSFKotlinFloatArray *)cepstra l:(int32_t)l __attribute__((swift_name("lifter(cepstra:l:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)logfbankSignal:(KSFKotlinFloatArray *)signal sampleRate:(int32_t)sampleRate winLen:(float)winLen winStep:(float)winStep nFilt:(int32_t)nFilt nfft:(int32_t)nfft lowFreq:(int32_t)lowFreq highFreq:(KSFInt * _Nullable)highFreq preemph:(float)preemph winFunc:(KSFKotlinFloatArray * _Nullable)winFunc __attribute__((swift_name("logfbank(signal:sampleRate:winLen:winStep:nFilt:nfft:lowFreq:highFreq:preemph:winFunc:)")));
- (double)mel2hzMel:(double)mel __attribute__((swift_name("mel2hz(mel:)")));
- (KSFKotlinDoubleArray *)mel2hzMel_:(KSFKotlinDoubleArray *)mel __attribute__((swift_name("mel2hz(mel_:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)mfccSignal:(KSFKotlinFloatArray *)signal sampleRate:(int32_t)sampleRate winLen:(float)winLen winStep:(float)winStep numCep:(int32_t)numCep nFilt:(int32_t)nFilt nfft:(KSFInt * _Nullable)nfft lowFreq:(int32_t)lowFreq highFreq:(KSFInt * _Nullable)highFreq preemph:(float)preemph ceplifter:(int32_t)ceplifter appendEnergy:(BOOL)appendEnergy winFunc:(KSFKotlinFloatArray * _Nullable)winFunc __attribute__((swift_name("mfcc(signal:sampleRate:winLen:winStep:numCep:nFilt:nfft:lowFreq:highFreq:preemph:ceplifter:appendEnergy:winFunc:)")));
- (KSFKotlinArray<KSFKotlinFloatArray *> *)sscSignal:(KSFKotlinFloatArray *)signal sampleRate:(int32_t)sampleRate winLen:(float)winLen winStep:(float)winStep nFilt:(int32_t)nFilt nfft:(int32_t)nfft lowFreq:(int32_t)lowFreq highFreq:(KSFInt * _Nullable)highFreq preemph:(float)preemph winFunc:(KSFKotlinFloatArray * _Nullable)winFunc __attribute__((swift_name("ssc(signal:sampleRate:winLen:winStep:nFilt:nfft:lowFreq:highFreq:preemph:winFunc:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Complex")))
@interface KSFComplex : KSFBase
- (instancetype)initWithReal:(double)real imag:(double)imag __attribute__((swift_name("init(real:imag:)"))) __attribute__((objc_designated_initializer));
- (double)abs __attribute__((swift_name("abs()")));
- (KSFComplex *)conjugate __attribute__((swift_name("conjugate()")));
- (KSFComplex *)cos __attribute__((swift_name("cos()")));
- (KSFComplex * _Nullable)dividesB:(KSFComplex *)b __attribute__((swift_name("divides(b:)")));
- (BOOL)isEqual:(id _Nullable)x __attribute__((swift_name("isEqual(_:)")));
- (KSFComplex * _Nullable)exp __attribute__((swift_name("exp()")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (double)im __attribute__((swift_name("im()")));
- (KSFComplex *)minusB:(KSFComplex *)b __attribute__((swift_name("minus(b:)")));
- (double)phase __attribute__((swift_name("phase()")));
- (KSFComplex *)plusB:(KSFComplex *)b __attribute__((swift_name("plus(b:)")));
- (KSFComplex * _Nullable)plusA:(KSFComplex *)a b:(KSFComplex *)b __attribute__((swift_name("plus(a:b:)")));
- (double)re __attribute__((swift_name("re()")));
- (KSFComplex *)reciprocal __attribute__((swift_name("reciprocal()")));
- (KSFComplex *)scaleAlpha:(double)alpha __attribute__((swift_name("scale(alpha:)")));
- (KSFComplex *)sin __attribute__((swift_name("sin()")));
- (KSFComplex * _Nullable)tan __attribute__((swift_name("tan()")));
- (KSFComplex *)timesB:(KSFComplex *)b __attribute__((swift_name("times(b:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("FFT")))
@protocol KSFFFT
@required
- (KSFKotlinArray<KSFComplex *> *)rfftSignal:(KSFKotlinFloatArray *)signal nfft:(int32_t)nfft __attribute__((swift_name("rfft(signal:nfft:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinFFT")))
@interface KSFKotlinFFT : KSFBase <KSFFFT>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (KSFKotlinArray<KSFComplex *> *)rfftSignal:(KSFKotlinFloatArray *)signal nfft:(int32_t)nfft __attribute__((swift_name("rfft(signal:nfft:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinFloatArray")))
@interface KSFKotlinFloatArray : KSFBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(KSFFloat *(^)(KSFInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (float)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (KSFKotlinFloatIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(float)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface KSFKotlinArray<T> : KSFBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(KSFInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<KSFKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntArray")))
@interface KSFKotlinIntArray : KSFBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(KSFInt *(^)(KSFInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int32_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (KSFKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinDoubleArray")))
@interface KSFKotlinDoubleArray : KSFBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(KSFDouble *(^)(KSFInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (double)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (KSFKotlinDoubleIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(double)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol KSFKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

__attribute__((swift_name("KotlinFloatIterator")))
@interface KSFKotlinFloatIterator : KSFBase <KSFKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (KSFFloat *)next __attribute__((swift_name("next()")));
- (float)nextFloat __attribute__((swift_name("nextFloat()")));
@end;

__attribute__((swift_name("KotlinIntIterator")))
@interface KSFKotlinIntIterator : KSFBase <KSFKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (KSFInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end;

__attribute__((swift_name("KotlinDoubleIterator")))
@interface KSFKotlinDoubleIterator : KSFBase <KSFKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (KSFDouble *)next __attribute__((swift_name("next()")));
- (double)nextDouble __attribute__((swift_name("nextDouble()")));
@end;

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
