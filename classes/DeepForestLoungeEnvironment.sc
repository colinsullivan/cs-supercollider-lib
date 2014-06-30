/**
 *  @file       DeepForestLoungeEnvironment.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *	@copyright	Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

DeepForestLoungeEnvironment : PerformanceEnvironment {
  modules_to_load {

    ^(
      randomHarpEnvironment: (
        class: RandomHarpSamplerEnvironment,
        initParams: (
          outputBus: 12,
          specs: (
            waitTime: ControlSpec(0.25, 10.0, \exp)
          )
        )
      ),
    );
  
  }
}
