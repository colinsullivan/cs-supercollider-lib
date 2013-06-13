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
      percussionOutputBus = 10,
      synthsOutputBus = 12,
      ambienceOutputBus = 18,
      freeOutputBusses = [14];

    this.modules = ();

    modulesToLoad = (
      clockEnvironment: (
        class: ClockEnvironment,
        initParams: (
        )
      ),
      randomizedLazersEnvironment: (
        class: RandomizedLazersEnvironment,
        initParams: (
          inChannel: 2,
          outputBus: percussionOutputBus
        )
      ),
      runningWaterEnvironment: (
        class: RunningWaterEnvironment,
        initParams: (
          outputBus: ambienceOutputBus
        )
      ),
      randomHarpEnvironment: (
        class: RandomHarpSamplerEnvironment,
        initParams: (
          outputBus: ambienceOutputBus
        )
      ),
     smoothbassEnvironment: (
        class: SmoothBassVoicerEnvironment,
        initParams: (
          inChannel: 3,
          outputBus: synthsOutputBus
        )
      ),
      fmPercussionEnvironment: (
        class: FMPercussionVoicerEnvironment,
        initParams: (
          inChannel: 4,
          outputBus: percussionOutputBus
        )
      ),
      granularChaosEnvironment: (
        class: GranularChaosEnvironment,
        initParams: (
          outputBus: 20
        )
      ),
      wideBassEnvironment: (
        class: WideBassVoicerEnvironment,
        initParams: (
          inChannel: 5,
          outputBus: 22
        )
      ),
      vileKickEnvironment: (
        class: VileKickEnvironment,
        initParams: (
          inChannel: 6,
          outputBus: 16
        )
      ),
      noisyVoicer: (
        class: NoisyVoicer,
        initParams: (
          inChannel: 6,
          outputBus: percussionOutputBus
        )
      ),
      impulsiveVoicer: (
        class: ImpulsiveVoicer,
        initParams: (
          inChannel: 7,
          outputBus: percussionOutputBus
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
      
      ("  * Loading " + moduleName).postln;
      ("----------------------------------------").postln;

      module = moduleProperties['class'].new(initParams);
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
