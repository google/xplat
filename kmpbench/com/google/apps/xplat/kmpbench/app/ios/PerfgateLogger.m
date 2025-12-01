#import "third_party/java_src/xplat/kmpbench/com/google/apps/xplat/kmpbench/app/ios/PerfgateLogger.h"
#import "googlemac/iPhone/Shared/Testing/Performance/Presto/Classes/Logger/PSTLogger.h"

@implementation PerfgateLogger {
  PSTLogger *_logger;
}

- (id)init {
  self = [super init];
  if (self != nil) {
    self->_logger = [[PSTLogger alloc] init];
  }
  return self;
}

- (void)logPerformance:(NSString *)name kt:(double)kt j2objc:(double)j2objc {
  _logger.testName = name;
  [_logger log:[PSTEntryBuilder builderWithBlock:^(PSTEntryBuilder *builder) {
             [builder add:@"kt" doubleValue:kt];
             [builder add:@"j2objc" doubleValue:j2objc];
           }]];
}

@end
