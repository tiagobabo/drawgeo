class PalettesController < ApplicationController
  # GET /palettes
  # GET /palettes.json
  def index
    @palettes = Palette.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render :json => @palettes }
    end
  end

  # GET /palettes/1
  # GET /palettes/1.json
  def show
    @palette = Palette.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => @palette }
    end
  end

  # GET /palettes/new
  # GET /palettes/new.json
  def new
    @palette = Palette.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render :json => @palette }
    end
  end

  # GET /palettes/1/edit
  def edit
    @palette = Palette.find(params[:id])
  end

  # POST /palettes
  # POST /palettes.json
  def create
    @palette = Palette.new(params[:palette])

    respond_to do |format|
      if @palette.save
        format.html { redirect_to @palette, :notice => 'Palette was successfully created.' }
        format.json { render :json => @palette, :status => :created, :location => @palette }
      else
        format.html { render :action => "new" }
        format.json { render :json => @palette.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /palettes/1
  # PUT /palettes/1.json
  def update
    @palette = Palette.find(params[:id])

    respond_to do |format|
      if @palette.update_attributes(params[:palette])
        format.html { redirect_to @palette, :notice => 'Palette was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @palette.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /palettes/1
  # DELETE /palettes/1.json
  def destroy
    @palette = Palette.find(params[:id])
    @palette.destroy

    respond_to do |format|
      format.html { redirect_to palettes_url }
      format.json { head :no_content }
    end
  end
end
