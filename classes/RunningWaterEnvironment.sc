RunningWaterEnvironment : PatchEnvironment {

  var <>hellValueBus,
    <>hellValueLabel,
    <>hellValueUpdater,
    <>hellFreqLabel,
    <>hellSyncButton,
    <>hellSyncOn;

  init {
    arg params;
    super.init(params);
    
    this.hellValueBus = Bus.control(numChannels: 1);

    this.hellSyncOn = false;
  }

  load_samples {
    arg callback;

    var sfxRoot = "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/",
      me = this;


    Buffer.read(Server.default, sfxRoot ++ "running water stream.aif", action: {
      arg buf;

      me.buf = buf;

      callback.value();
    });
  }

  load_patch {
    super.load_patch();

    this.patch = Patch(Instr.at("sfx.RunningWaterStreamAutomated"), (
      buffer: this.buf,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      useOscillator: KrNumberEditor.new(0, \gate.asSpec()),
      hellValueBus: this.hellValueBus
    ));
    this.patch.prepareForPlay();
  }

  init_gui {
    arg params;

    var toggleButton,
      patch = this.patch,
      layout = params['layout'],
      interface = this.interface,
      labelWidth = 50,
      me = this;

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("amp", layout, labelWidth);
      patch.amp.gui(layout);
      layout.startRow();

      ArgNameLabel("hellMin", layout, labelWidth);
      patch.hellMin.gui(layout);
      layout.startRow();

      ArgNameLabel("hellMax", layout, labelWidth);
      patch.hellMax.gui(layout);
      layout.startRow();

      "patch.hellFreq.class:".postln;
      patch.hellFreq.class.postln;

      patch.hellFreq.activeValue_({ arg val;
        patch.hellFreq.value_(val);
        patch.hellFreq.action.value(patch.hellFreq.value);

        "val:".postln;
        val.postln;
      });

      ArgNameLabel("hellFreq", layout, labelWidth);
      Knob.new(layout, Rect(0, 0, 25, 25))
        .action_({
          arg knob;
          var newFreqVal;

          if (knob.value() == 0, {
            patch.set(\useOscillator, 0);
          }, {
            patch.set(\useOscillator, 1);
            newFreqVal = patch.hellFreq.spec.map(knob.value());

            patch.set(\hellFreq, newFreqVal);
            me.hellFreqLabel.string = newFreqVal.round(0.01);
          });

        });
      
      this.hellFreqLabel = ArgNameLabel("", layout, labelWidth);
      this.hellFreqLabel.background = Color.black();
      this.hellFreqLabel.stringColor = Color.green();

      this.hellSyncButton = Button(layout, Rect(0, 0, 50, 50))
        .states_([
          ["T"],
          ["T", Color.green, Color.black]
        ])
        .action_({
          arg syncButton;

          if (syncButton.value(), {

            this.hellSyncOn = false;
          
          }, {

            this.hellSyncOn = true;
          
          });

        });

      layout.startRow();

    });

    layout.flow({
      arg layout;

      toggleButton = Button(layout, Rect(10, 10, 100, 30))
        .states_([
          ["on"],
          ["off"]
        ])
        .action_({
          arg toggleButton;

          patch.set(\gate, toggleButton.value);
        });

    });

    layout.flow({
      arg layout;

      ArgNameLabel("hellValue", layout, labelWidth);
      me.hellValueLabel = ArgNameLabel("", layout, labelWidth);
      me.hellValueLabel.background = Color.black();
      me.hellValueLabel.stringColor = Color.green();

    });

    this.hellValueUpdater = Routine.new({

      {
        {
          me.window.isClosed.not.if {
            me.hellValueBus.get({
              arg hellValue;
 
              {me.hellValueLabel.string = " " + hellValue.round(0.01);}.defer();
  
            });
          };
  
        }.defer();
        
  
        0.01.wait;
      }.loop();
    });

    this.hellValueUpdater.play();

    this.window.onClose = { me.hellValueUpdater.stop(); };

    /*{
      fork {
        while { me.window.isClosed.not } {
          me.hellValueBus.get({
            arg hellValue;
  
            "hellValue:".postln;
            hellValue.postln;
          });
  
          0.01.wait;
        }
      };
    }.defer();*/

  }

  load_external_controller_mappings {
    
    super.load_external_controller_mappings();

    this.map_uc33_to_patch('sl2', \amp);
  }

}
