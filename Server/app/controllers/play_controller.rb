class PlayController < ApplicationController
	def guess
		@player = User.find(params[:id])
		@draw = Draw.find(params[:draw_id])
		@guess = params[:guess]
		@word = Word.find(@draw.word_id)
		@author = User.find(@draw.id_creator)
		@draw_user = DrawUser.where("id_draw = ? AND id_user= ?", params[:draw_id], params[:id])

		if(@draw_user.empty?)
			if(@player.id != @author.id)
				if(@guess == @word.word)
					@draw_user = DrawUser.new(:id_draw => @draw.id, :id_user => @player.id)
					if @draw_user.save
						@player.update_attribute(:num_done, @player.num_done + 1)
						@author.update_attribute(:num_success, @author.num_success + 1)
						@player.update_attribute(:piggies, @player.piggies + (@word.difficulty * 2))
						@author.update_attribute(:piggies, @author.piggies + (@word.difficulty))
					end

					if(@draw.challenge)
						@player.update_attribute(:piggies, @player.piggies + 1)
						@author.update_attribute(:piggies, @author.piggies + 1)
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
	end

	def getUserByEmail
		@user = User.where("email = ?", params[:email])

		if(!@user.nil? && !@user.empty?)
			respond_to do |format|
		      	format.json { render :json => {:status => "Ok", :user => @user.first}.to_json }
		    end
		else
			respond_to do |format|
		      	format.json { render :json => {:status => "User not found."}.to_json }
		    end
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

end
