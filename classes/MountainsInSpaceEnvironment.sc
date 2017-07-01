/**
 *  @file       MountainsInSpaceEnvironment.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *	@copyright	Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

MountainsInSpaceEnvironment : PerformanceEnvironment {

  modules_to_load {
    var freeOutputBusses = [38, 40, 42, 44, 46];

    ^(
      //clockEnvironment: (
        //class: ClockEnvironment,
        //initParams: (
        //)
      //),
      /*noisyVoicer: (
        class: NoisyVoicer,
        initParams: (
          inChannel: 6,
          outputBus: 10
        )
      ),*/
      //softSynth: (
        //class: SoftSynthComponent,
        //initParams: (
          //inChannel: 8,
          //outputBus: 12
        //)
      //),
      //impulsiveVoicer: (
        //class: ImpulsiveVoicer,
        //initParams: (
          //inChannel: 7,
          //outputBus: 14
        //)
      //),
      //vileKickEnvironment: (
        //class: VileKickEnvironment,
        //initParams: (
          //inChannel: 6,
          //outputBus: 16
        //)
      //),
      runningWaterEnvironment: (
        class: RunningWaterEnvironment,
        initParams: (
          outputBus: 18
        )
      ),
      granularChaosEnvironment: (
        class: GranularChaosEnvironment,
        initParams: (
          outputBus: 20
        )
      ),
      //wideBassEnvironment: (
        //class: WideBassVoicerEnvironment,
        //initParams: (
          //inChannel: 5,
          //outputBus: 22
        //)
      //),
      /*smoothbassEnvironment: (
        class: SmoothBassVoicerEnvironment,
        initParams: (
          inChannel: 3,
          outputBus: 24
        )
      ),*/
      randomHarpEnvironment: (
        class: RandomHarpSamplerEnvironment,
        initParams: (
          outputBus: 26
        )
      ),
      //randomizedLazersEnvironment: (
        //class: RandomizedLazersEnvironment,
        //initParams: (
          //inChannel: 2,
          //outputBus: 28
        //)
      //),
      //hardSineVoicer: (
        //class: HardSineVoicerComponent,
        //initParams: (
          //inChannel: 9,
          //outputBus: 36
        //)
      //),
      /*fmPercussionEnvironment: (
        class: FMPercussionVoicerEnvironment,
        initParams: (
          inChannel: 4,
          outputBus: 30
        )
      ),*/
      /*pannerEnvironment: (
        class: AbletonPannerEnvironment,
        initParams: ()
      ),*/
      //synkopater: (
        //class: SynkopaterDelay,
        //initParams: (
          //outputBus: 32
        //)
      //),
      //synkopants: (
        //class: Synkopants,
        //initParams: (
          //outputBus: 34
        //)
      //),
    );

  }

}
