class DrawUsersController < ApplicationController
  # GET /draw_users
  # GET /draw_users.json
  def index
    @draw_users = DrawUser.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render :json => @draw_users }
    end
  end

  # GET /draw_users/1
  # GET /draw_users/1.json
  def show
    @draw_user = DrawUser.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => @draw_user }
    end
  end

  # GET /draw_users/new
  # GET /draw_users/new.json
  def new
    @draw_user = DrawUser.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render :json => @draw_user }
    end
  end

  # GET /draw_users/1/edit
  def edit
    @draw_user = DrawUser.find(params[:id])
  end

  # POST /draw_users
  # POST /draw_users.json
  def create
    @draw_user = DrawUser.new(params[:draw_user])

    respond_to do |format|
      if @draw_user.save
        format.html { redirect_to @draw_user, :notice => 'Draw user was successfully created.' }
        format.json { render :json => @draw_user, :status => :created, :location => @draw_user }
      else
        format.html { render :action => "new" }
        format.json { render :json => @draw_user.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /draw_users/1
  # PUT /draw_users/1.json
  def update
    @draw_user = DrawUser.find(params[:id])

    respond_to do |format|
      if @draw_user.update_attributes(params[:draw_user])
        format.html { redirect_to @draw_user, :notice => 'Draw user was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @draw_user.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /draw_users/1
  # DELETE /draw_users/1.json
  def destroy
    @draw_user = DrawUser.find(params[:id])
    @draw_user.destroy

    respond_to do |format|
      format.html { redirect_to draw_users_url }
      format.json { head :no_content }
    end
  end
end
