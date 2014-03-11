PerformanceEnvironment : Object {

  var 
    /**
     *  Dictionary storing all loaded modules indexed by name.
     **/
    <>modules;

  *new {
    ^super.new.init();
  }

  init {
    var modulesToLoad,
      freeOutputBusses = [36, 38, 40, 42, 44, 46];

    this.modules = ();

    modulesToLoad = (
      clockEnvironment: (
        class: ClockEnvironment,
        initParams: (
        )
      ),
      /*noisyVoicer: (
        class: NoisyVoicer,
        initParams: (
          inChannel: 6,
          outputBus: 10
        )
      ),*/
      /*softSynth: (
        class: SoftSynthComponent,
        initParams: (
          inChannel: 8,
          outputBus: 12
        )
      ),*/
      impulsiveVoicer: (
        class: ImpulsiveVoicer,
        initParams: (
          inChannel: 7,
          outputBus: 14
        )
      ),
      /*vileKickEnvironment: (
        class: VileKickEnvironment,
        initParams: (
          inChannel: 6,
          outputBus: 16
        )
      ),*/
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
      /*wideBassEnvironment: (
        class: WideBassVoicerEnvironment,
        initParams: (
          inChannel: 5,
          outputBus: 22
        )
      ),*/
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
      /*randomizedLazersEnvironment: (
        class: RandomizedLazersEnvironment,
        initParams: (
          inChannel: 2,
          outputBus: 28
        )
      ),*/
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
      synkopater: (
        class: Synkopater,
        initParams: (
          outputBus: 32
        )
      ),
      synkopaterTwo: (
        class: SecondSynkopater,
        initParams: (
          outputBus: 34
        )
      )
    );

    this.load_modules(modulesToLoad);
  }

  load_modules {
    arg modulesToLoad;
    var me = this;

    modulesToLoad.keysValuesDo({
      arg moduleName, moduleProperties;
      var module, initParams;

      initParams = moduleProperties['initParams'];

      initParams['origin'] = 0@0;
      initParams['init_done_callback'] = {
        me.modules[moduleName] = module;

        if (me.modules.size == modulesToLoad.size, {
          me.modules_all_loaded();
        });
      };
      
      ("  * Loading" + moduleName ++ "...").postln;

      module = moduleProperties['class'].new(initParams);
      
      ("  * Loaded.").postln();
      ("  * Output channel:" + initParams[\outputBus]).postln();
      ("----------------------------------------").postln;
    });
  }

  modules_all_loaded {
    var screenHeight = 960,
      screenWidth = 1440,
      instrX,
      instrY;
    
    instrY = screenHeight - 34;
    instrX = 0;

    // layout windows
    this.modules.keysValuesDo({
      arg moduleName, module;
      
      instrY = instrY - module.window.bounds.height;

      module.window.bounds.moveTo(instrX@instrY);
    });


    /*this.modules['clockEnvironment'].request_tempo_update();*/
  }
}
