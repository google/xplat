#import <Foundation/Foundation.h>

@interface PerfgateLogger : NSObject

- (void)logPerformance:(NSString *)name kt:(double)kt j2objc:(double)j2objc;

@end