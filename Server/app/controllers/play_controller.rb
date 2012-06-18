class PlayController < ApplicationController

#
# Try and guess a word
#
# * *Args*    :
#   - +id+ -> user's id
#   - +draw_id+ -> draw's id
#   - +guess+ -> the guess from the user
# * *Returns* :
#   - {"status":"Ok", "word" : {" [date] ","difficulty": [level of difficulty] ,"id": [word's id] ,"updated_at":" [date] ","word":" [word] "}} 
#   - {"status":"Incorrect guess."}
#   - {"status":"You can't guess your own word."}
#   - {"status":"Draw already guessed."}
#   - {"status":"User doesn't exist."}
#
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

#
# Get user information by email
#
# * *Args*    :
#   - +email+ -> user's email
# * *Returns* :
#   - {"status":"Ok","user":{"created_at":" [date] ","email":" [email] ","id": [id] ,"id_avatar": [avatar's id] ,"keys": [number of keys] ,"name":" [user's name] ","num_created": [number of draw's created] ,"num_done": [number of draw's guessed] ,"num_success": [number of draw's guessed by others] ,"piggies": [number of piggies] ,"ranking": [accumulative number of piggies] ,"updated_at":" [date] "},"avatar":{"created_at":" [date] ","id": [avatar's id] ,"updated_at":" [date] ","url":" [image url] "}} 
#   - {"status":"User created.","user":{"created_at":" [date] ","email":" [email] ","id": [id] ,"id_avatar": [avatar's id] ,"keys": [number of keys] ,"name":" [user's name] ","num_created": [number of draw's created] ,"num_done": [number of draw's guessed] ,"num_success": [number of draw's guessed by others] ,"piggies": [number of piggies] ,"ranking": [accumulative number of piggies] ,"updated_at":" [date] "},"avatar":{"created_at":" [date] ","id": [avatar's id] ,"updated_at":" [date] ","url":" [image url] "}} 
#
	def getUserByEmail
		@user = User.where("email = ?", params[:email])
		if(!@user.nil? && !@user.empty?)
			respond_to do |format|
		      	format.json { render :json => {:status => "Ok", :user => @user.first, :avatar => Avatar.find(@user.first.id_avatar), :created => @user.first.num_created }.to_json }
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


#
# Get user's palette by Id
#
# * *Args*    :
#   - +id+ -> user id
# * *Returns* :
#   - Array [{"hex1":string,"hex2":string,"hex3":string,"hex4":string,"cost":int,"id":int}]
#
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


#
# Get three new words (one easy, one medium and one hard)
#
# * *Returns* :
#   - {"status": "Ok", "easy":{ easy_word }, "medium":{ medium_word }, "hard" :{ hard_word } } 
#
	def getNewWords
		@easy = Word.where("difficulty = ?", 1).sample
		@medium = Word.where("difficulty = ?", 2).sample
		@hard = Word.where("difficulty = ?", 3).sample

		respond_to do |format|
	      	format.json { render :json => {:status => "Ok", :easy => @easy, :medium => @medium, :hard => @hard }.to_json }
	    end
	end


#
# Change user's avatar
#
# * *Args*    :
#   - +id+ -> user id
#   - +id_avatar+ -> avatar's id
# * *Returns* :
#   - {"status" : "Avatar changed."} 
#   - {"status" : "Avatar doesn't exist."}
#   - {"status" : "User doesn't exist."}  
#
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


#
# Buy a new palette to a user
#
# * *Args*    :
#   - +id+ -> user id
#   - +id_palette+ -> palette's id
# * *Returns* :
#   - {"status" : "Palette bought successfully."} 
#   - {"status" : "You don't have enough piggies."} 
#   - {"status" : "You already have this palette."}
#   - {"status" : "User/Palette doesn't exist."} 
#
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


#
# Gets all palettes in the system
#
# * *Returns* :
#   - Array [{"hex1":string,"hex2":string,"hex3":string,"hex4":string,"cost":int,"id":int}]
#
	def getPalettes
		respond_to do |format|
	      	format.json { render :json => Palette.all }
	    end
	end


#
# Add a new draw in a location
#
# * *Args*    :
#   - +id_creator+ -> id of the draw's creator
#   - +word_id+ -> word's id
#   - +latitude+ -> location's latitude
#   - +longitude+ -> location's longitude
#   - +draw+ -> draw information
#   - +drawx+ -> drawx information
#   - +drawy+ -> drawy information
#   - +challenge+ -> challenge (true or false)
#   - +password+ -> challenge's password
#   - +description+ -> challenge's description
#   - +xdensity+ -> screen x density
#   - +ydensity+ -> screen y density
# * *Returns* :
#   - {"status" : "Draw added."} 
#   - {"status" : "User or word doesn't exist."}
#
	def addNewDraw
		@creator = User.find(params[:id_creator])
		@word = Word.find(params[:word_id])
		if(!@creator.nil? && !@word.nil?)
			@new_draw = Draw.new(:id_creator => @creator.id, :word_id => @word.id, :latitude => params[:latitude], :longitude => params[:longitude], :draw => params[:draw], :drawx => params[:drawx], :drawy => params[:drawy], :challenge => params[:challenge], :description => params[:description], :password => params[:password], :xdensity => params[:xdensity], :ydensity => params[:ydensity]  )
			if @new_draw.save
				@creator.update_attribute(:num_created, @creator.num_created + 1)
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

#
# Replace a draw after the one before has been guessed
#
# * *Args*    :
#   - +id+ -> user's id
#   - +draw_id+ -> draw's id
#   - +word_id+ -> word's id
#   - +draw+ -> draw information
#   - +drawx+ -> drawx information
#   - +drawy+ -> drawy information
#   - +challenge+ -> challenge (true or false)
#   - +password+ -> challenge's password
#   - +description+ -> challenge's description
#   - +xdensity+ -> screen x density
#   - +ydensity+ -> screen y density
#   - +id_creator+ -> id of the draw's creator
# * *Returns* :
#   - {"status" : "Draw has been updated."}
#   - {"status" : "User or draw doesn't exist."}
#
	def replace
		@user = User.find(params[:id])
		@draw = Draw.find(params[:draw_id])
		@word = Word.find(params[:word_id])
		if(!@user.nil? && !@draw.nil? && !@word.nil?)
			@draw.update_attribute(:word_id, @word.id)
			@draw.update_attribute(:draw, params[:draw])
			@draw.update_attribute(:drawx, params[:drawx])
			@draw.update_attribute(:drawy, params[:drawy])
			@draw.update_attribute(:challenge, params[:challenge])
			@draw.update_attribute(:password, params[:password])
			@draw.update_attribute(:description, params[:description])
			@draw.update_attribute(:xdensity, params[:xdensity])
			@draw.update_attribute(:ydensity, params[:ydensity])
			@draw.update_attribute(:id_creator, @user.id)
			@draw_users = DrawUser.where("id_draw = ?", params[:draw_id])
			@draw_users.each.each do|n|
			  DrawUser.delete(n.id)
			end
			@user.update_attribute(:num_created, @user.num_created + 1)
			respond_to do |format|
		      	format.json { render :json => {:status => "Draw has been updated."}.to_json }
		    end
		else
			respond_to do |format|
		      	format.json { render :json => {:status => "User or draw doesn't exist."}.to_json }
		    end
		end
	end
end
