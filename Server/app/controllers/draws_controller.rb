class DrawsController < ApplicationController
  # GET /draws
  # GET /draws.json
  def index
    @draws = Draw.all
    @nearbys = Draw.near('Porto, Portugal', 300, {:order => "distance", :units => :km})

    respond_to do |format|
      format.html # index.html.erb
      format.json { render :json => @draws }
    end
  end

  # GET /draws/1
  # GET /draws/1.json
  def show
    @draw = Draw.find(params[:id])
    @guess = getGuess(@draw,16)
    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => {:draw => @draw, :guess => @guess, :creator => User.find(@draw.id_creator), :times_guessed => DrawUser.where("id_draw = ?", @draw.id).length, :piggies => (Word.find(@draw.word_id).difficulty * 2), :word => Word.find(@draw.word_id).word  }.to_json }
    end
  end

  # GET /draws/new
  # GET /draws/new.json
  def new
    @draw = Draw.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render :json => @draw }
    end
  end

  # GET /draws/1/edit
  def edit
    @draw = Draw.find(params[:id])
    @distance = @draw.distance_to('Porto, Portugal', units = :km)
  end

  # POST /draws
  # POST /draws.json
  def create
    @draw = Draw.new(params[:draw])

    respond_to do |format|
      if @draw.save
        format.html { redirect_to @draw, :notice => 'Draw was successfully created.' }
        format.json { render :json => @draw, :status => :created, :location => @draw }
      else
        format.html { render :action => "new" }
        format.json { render :json => @draw.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /draws/1
  # PUT /draws/1.json
  def update
    @draw = Draw.find(params[:id])

    respond_to do |format|
      if @draw.update_attributes(params[:draw])
        format.html { redirect_to @draw, :notice => 'Draw was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @draw.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /draws/1
  # DELETE /draws/1.json
  def destroy
    @draw = Draw.find(params[:id])
    @draw.destroy

    respond_to do |format|
      format.html { redirect_to draws_url }
      format.json { head :no_content }
    end
  end

  def getGuess(draw,num)
    @word = Word.find(draw.word_id)
    @guess = @word.word.split(//)
    @num_words = num-@guess.length
    @abc = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]
    @num_words.times{ @guess.push(@abc.sample) }
    @guess = @guess.shuffle
  end

end
