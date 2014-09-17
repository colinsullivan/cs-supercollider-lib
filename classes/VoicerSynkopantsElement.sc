OrganicPercussionSynkopantsElement : SynkopantsElement {
  var <>numVoices,
    <>noteDurationCtl,
    <>freq;

  init {

    arg params;

    super.init(params);

    this.freq = params['freq'];

    this.noteDurationCtl = KrNumberEditor.new(0.5, \unipolar);
  }

  init_patch {

    this.patch = Patch("cs.fm.OrganicPercussion", (
      freq: this.freq,
      amp: this.ampSlider,
      autoDurationOn: 0,
      noteDuration: this.noteDurationCtl,
      gate: 1
    ));
  
  }

  init_gui {
    arg params;

    var layout,
      labelWidth;

    super.init_gui(params);

    layout = params['layout'];
    labelWidth = params['labelWidth'];
    
    ArgNameLabel("noteDuration", layout, labelWidth);
    this.noteDurationCtl.gui(layout);
    layout.startRow();
  }
}
