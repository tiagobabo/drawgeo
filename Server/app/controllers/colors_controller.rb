class ColorsController < ApplicationController
  def create
      @user = User.find(params[:user_id])
      @color = @user.colors.create(params[:color])
      redirect_to user_colors_path(@user)
    end

    def index
      @user = User.find(params[:user_id])
      @colors = @user.colors

      respond_to do |format|
          format.html # index.html.erb
          format.json { render :json => @colors }
      end
    end

  # GET /colors/1
  # GET /colors/1.json
  def show
    @color = Color.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => @color }
    end
  end

  # GET /users/new
  # GET /users/new.json
  def new
    @user = User.find(params[:user_id])
    @color = Color.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render :json => @color }
    end
  end

  # GET /colors/1/edit
  def edit
    @color = Color.find(params[:id])
    @user = User.find(@color.user.id)
  end

  # PUT /colors/1
  # PUT /colors/1.json
  def update
    @color = Color.find(params[:id])

    respond_to do |format|
      if @color.update_attributes(params[:color])
        format.html { redirect_to @color, :notice => 'Color was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @color.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /colors/1
  # DELETE /colors/1.json
  def destroy
    @color = Color.find(params[:id])
    @user_id = @color.user.id
    @color.destroy

    respond_to do |format|
      format.html { redirect_to user_colors_path(@user_id) }
      format.json { head :no_content }
    end
  end
end
