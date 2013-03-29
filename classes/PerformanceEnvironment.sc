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
    var modulesToLoad;

    this.modules = ();

    modulesToLoad = (
      clockEnvironment: (
        class: ClockEnvironment
      ),
      randomizedLazersEnvironment: (
        class: RandomizedLazersEnvironment
      )
    );

    this.load_modules(modulesToLoad);
  }

  load_modules {
    arg modulesToLoad;
    var me = this,
      screenHeight = 960,
      screenWidth = 1440,
      instrX,
      instrY;

    instrY = screenHeight - 34;
    instrX = 0;

    modulesToLoad.keysValuesDo({
      arg moduleName, moduleProperties;

      var module;

      module = moduleProperties['class'].new((
        origin: instrX@instrY,
        init_done_callback: {
          me.modules[moduleName] = module;

          if (me.modules.size == modulesToLoad.size, {
            me.modules_all_loaded();    
          });

        }
      ));

      instrY = instrY - module.window.bounds.height;

    });
    /*this.clockEnvironment = ClockEnvironment.new((
      origin: instrX@screenHeight,
      init_done_callback: {

        instrX = me.clockEnvironment.window.bounds.width;

        me.granularChaosEnvironment = GranularChaosEnvironment.new((
          origin: instrX@instrY,
          init_done_callback: {

            instrY = instrY - 25 - me.granularChaosEnvironment.window.bounds.height;
            
            me.runningWaterEnvironment = RunningWaterEnvironment.new((
              origin: instrX@instrY,
              init_done_callback: {

                instrY = instrY - 25 - me.runningWaterEnvironment.window.bounds.height;

                me.circularWarpInputEnvironment = CircularWarpInputEnvironment.new((
                  origin: instrX@instrY,
                  
                  init_done_callback: {
                
                    instrY = instrY + 800 - me.circularWarpInputEnvironment.window.bounds.height;
                    
                    me.dubBassEnvironment = DubbassVoicerEnvironment.new((
                      origin: instrX@instrY,
                      init_done_callback: {
                        instrY = instrY - me.dubBassEnvironment.window.bounds.height;

                        me.rhodesVoicerEnvironment = RhodesVoicerEnvironment.new((
                          origin: instrX@instrY,
                          init_done_callback: {
                            instrY = instrY - me.rhodesVoicerEnvironment.window.bounds.height;

                            me.randomizedLazersEnvironment = RandomizedLazersEnvironment.new((
                              origin: instrX@instrY
                            ));
                          }
                        ));
                      
                      }
                    ));
                  }
                ));
              }
            ));

          }
        ));
      }
    ));*/
    /*this.sequencedGranularEnvironment = SequencedGranularEnvironment.new();*/

  }

  modules_all_loaded {
    this.modules['clockEnvironment'].request_tempo_update();
  }
}
