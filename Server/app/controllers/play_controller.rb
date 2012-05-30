class PlayController < ApplicationController
	def guess
		@player = User.find(params[:id])
		@draw = Draw.find(params[:draw_id])
		@guess = params[:guess]
		@word = Word.find(@draw.word_id)
		@author = User.find(@draw.id_creator)
		@draw_user = DrawUser.where("id_draw = ? AND id_user= ?", params[:draw_id], params[:id])
		if(!@player.nil?)
			if(@draw_user.empty?)
				if(@player.id != @author.id)
					if(@guess == @word.word)
						@draw_user = DrawUser.new(:id_draw => @draw.id, :id_user => @player.id)
						if @draw_user.save
							@player.update_attribute(:num_done, @player.num_done + 1)
							@author.update_attribute(:num_success, @author.num_success + 1)
							@player.update_attribute(:piggies, @player.piggies + (@word.difficulty * 2))
							@player.update_attribute(:ranking, @player.ranking + (@word.difficulty * 2))
							@author.update_attribute(:piggies, @author.piggies + (@word.difficulty))
							@author.update_attribute(:ranking, @author.ranking + (@word.difficulty))
						end

						if(@draw.challenge)
							@player.update_attribute(:piggies, @player.piggies + 1)
							@player.update_attribute(:ranking, @player.ranking + 1)
							@author.update_attribute(:piggies, @author.piggies + 1)
							@author.update_attribute(:ranking, @author.ranking + 1)
						end
						respond_to do |format|
					      format.json { render :json => {:status => "Ok", :word => @word}.to_json }
					    end
					else
						respond_to do |format|
					      format.json { render :json => {:status => "Incorrect guess."}.to_json }
					    end
					end
				else
					respond_to do |format|
				      format.json { render :json => {:status => "You can't guess your own word."}.to_json }
				    end
				end
			else
				respond_to do |format|
			      format.json { render :json => {:status => "Draw already guessed."}.to_json }
			    end
			end
		else
			respond_to do |format|
		      format.json { render :json => {:status => "User doesn't exist."}.to_json }
		    end
		end
	end

	def getUserByEmail
		@user = User.where("email = ?", params[:email])
		if(!@user.nil? && !@user.empty?)
			respond_to do |format|
		      	format.json { render :json => {:status => "Ok", :user => @user.first, :avatar => Avatar.find(@user.first.id_avatar), :created => Draw.where("id_creator = ?", @user.first.id).length }.to_json }
		    end
		else
			@user_new = User.new(:email => params[:email])
			if @user_new.save
				respond_to do |format|
			      	format.json { render :json => {:status => "User created.", :user => @user_new, :avatar => Avatar.find(@user_new.id_avatar), :created => 0}.to_json }
			    end
			end
		end
	end

	def getPaletteByUser
		@paletteUsers = PaletteUser.where("id_user = ?", params[:id])
		@palettes = Array.new
	    @paletteUsers.each do |paletteUser|
	      @palettes.push(Palette.find(paletteUser.id_palette))
	    end
	    respond_to do |format|
	      	format.json { render :json => @palettes }
	    end
	end

	def getNewWords
		@easy = Word.where("difficulty = ?", 1).sample
		@medium = Word.where("difficulty = ?", 2).sample
		@hard = Word.where("difficulty = ?", 3).sample

		respond_to do |format|
	      	format.json { render :json => {:status => "Ok", :easy => @easy, :medium => @medium, :hard => @hard }.to_json }
	    end
	end

	def changeAvatar
		@player = User.find(params[:id])
		@avatar = Avatar.find(params[:id_avatar])
		if(!@player.nil?)
			if(!@avatar.nil?)
				@player.update_attribute(:id_avatar, @avatar.id)
				respond_to do |format|
			      	format.json { render :json => {:status => "Avatar changed."}.to_json }
			    end
			else
				respond_to do |format|
			      	format.json { render :json => {:status => "Avatar doesn't exist."}.to_json }
			    end
			end
		else
			respond_to do |format|
		      	format.json { render :json => {:status => "User doesn't exist."}.to_json }
		    end	
		end
	end

	def buyNewPalette
		@user = User.find(params[:id])
		@palette = Palette.find(params[:id_palette])
		if(!@user.nil? && !@palette.nil?)
			@check = PaletteUser.where("id_user = ? AND id_palette = ?", params[:id],params[:id_palette])
			if(@check.empty?)
				if(@user.piggies >= @palette.cost)
					@user.update_attribute(:piggies, @user.piggies - @palette.cost)
					@user_palette_new = PaletteUser.new(:id_palette => params[:id_palette], :id_user => params[:id])
					if @user_palette_new.save
						respond_to do |format|
					      	format.json { render :json => {:status => "Palette bought successfully."}.to_json }
					    end
					end
				else
					respond_to do |format|
				      	format.json { render :json => {:status => "You don't have enough piggies."}.to_json }
				    end
				end
			else
				respond_to do |format|
			      	format.json { render :json => {:status => "You already have this palette."}.to_json }
			    end
			end
		else
			respond_to do |format|
		      	format.json { render :json => {:status => "User/Palette doesn't exist."}.to_json }
		    end	
		end
	end

	def addNewDraw
		@creator = User.find(params[:id_creator])
		@word = Word.find(params[:word_id])
		if(!@creator.nil? && !@word.nil?)
			@new_draw = Draw.new(:id_creator => @creator.id, :word_id => @word.id, :latitude => params[:latitude], :longitude => params[:longitude], :draw => params[:draw], :drawx => params[:drawx], :drawy => params[:drawy], :challenge => params[:challenge], :description => params[:description], :password => params[:password]  )
			if @new_draw.save
				respond_to do |format|
			      	format.json { render :json => {:status => "Draw added."}.to_json }
			    end
			end
		else
			respond_to do |format|
		      	format.json { render :json => {:status => "User or word doesn't exist."}.to_json }
		    end
		end
	end
end
